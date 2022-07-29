package site.iplease.iadsmserver.domain.demand.subscriber

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import site.iplease.iadsmserver.domain.demand.service.DemandErrorService
import site.iplease.iadsmserver.domain.demand.util.DemandAcceptErrorOnDemandConverter
import site.iplease.iadsmserver.global.demand.message.IpAssignDemandAcceptErrorOnDemandMessage
import site.iplease.iadsmserver.global.demand.subscriber.IpAssignDemandAcceptErrorOnDemandSubscriber

@Component
class IpAssignDemandAcceptErrorOnDemandSubscriberV1(
    private val demandAcceptErrorOnDemandConverter: DemandAcceptErrorOnDemandConverter,
    private val demandErrorService: DemandErrorService
): IpAssignDemandAcceptErrorOnDemandSubscriber {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun subscribe(message: IpAssignDemandAcceptErrorOnDemandMessage) {
        demandAcceptErrorOnDemandConverter.convert(message)
            .flatMap { demand -> demandErrorService.handle(demand) }
            .doOnSuccess { logRollback() }
            .doOnError { logError(it) }
            .onErrorResume { Mono.empty() }
            .block()
    }

    private fun logRollback() {
        logger.info("신청 수락에 대한 보상트랜젝션을 성공적으로 수행하였습니다!")
    }

    private fun logError(throwable: Throwable) {
        logger.info("신청 수락에 대한 보상트랜젝션을 수행하던중 오류가 발생하였습니다!")
        logger.info("exception: ${throwable::class.simpleName}")
        logger.info("message: ${throwable.localizedMessage}")
        logger.trace("stacktrace")
        logger.trace(throwable.stackTraceToString())
    }
}