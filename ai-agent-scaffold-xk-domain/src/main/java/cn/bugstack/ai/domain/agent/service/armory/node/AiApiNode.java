package cn.bugstack.ai.domain.agent.service.armory.node;

import cn.bugstack.ai.domain.agent.model.entity.ArmoryCommandEntity;
import cn.bugstack.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import cn.bugstack.ai.domain.agent.model.valobj.AiAgentRegisterVO;
import cn.bugstack.ai.domain.agent.service.armory.AbstractArmorySupport;
import cn.bugstack.ai.domain.agent.service.armory.factory.DefaultArmoryFactory;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class AiApiNode extends AbstractArmorySupport {
    @Resource
    private ChatModelNode chatModelNode;

    @Override
    protected AiAgentRegisterVO doApply(ArmoryCommandEntity requestParameter, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        log.info("Armory Node :AiApiNode-Armory");
        AiAgentConfigTableVO aiAgentConfigTableVO = requestParameter.getAiAgentConfigTableVO();

        AiAgentConfigTableVO.Module.AiApi aiApi = aiAgentConfigTableVO.getModule().getAiApi();

        OpenAiApi openAiApi = OpenAiApi.builder()
                .baseUrl(aiApi.getBaseUrl())
                .apiKey(aiApi.getApiKey())
                .completionsPath(aiApi.getCompletionsPath())
                .embeddingsPath(aiApi.getEmbeddingsPath())
                .build();
        dynamicContext.setAiApi(openAiApi);
        // 路由到下一个节点，如果不需要路由了，可以 return 返回结果
        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryFactory.DynamicContext, AiAgentRegisterVO> get(ArmoryCommandEntity requestParameter, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {

        // 如果不需要下一个节点了，可以配置 defaultStrategyHandler
        return chatModelNode;
    }

}
