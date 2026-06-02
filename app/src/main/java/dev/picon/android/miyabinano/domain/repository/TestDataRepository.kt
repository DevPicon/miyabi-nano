package dev.picon.android.miyabinano.domain.repository

import dev.picon.android.miyabinano.domain.model.InferenceCapability
import dev.picon.android.miyabinano.domain.model.TestCase
import dev.picon.android.miyabinano.domain.model.TestCategory

object TestDataRepository {

    val summarizationTests = listOf(
        TestCase(
            id = "sum_tech_1",
            name = "Technical Article",
            capability = InferenceCapability.SUMMARIZATION,
            inputText = """
                Machine learning is a subset of artificial intelligence that enables computers to learn and improve from experience without being explicitly programmed. It focuses on the development of computer programs that can access data and use it to learn for themselves. The process of learning begins with observations or data, such as examples, direct experience, or instruction, in order to look for patterns in data and make better decisions in the future based on the examples that we provide. The primary aim is to allow the computers to learn automatically without human intervention or assistance and adjust actions accordingly.
            """.trimIndent(),
            category = TestCategory.TECHNICAL
        ),
        TestCase(
            id = "sum_business_1",
            name = "Business News",
            capability = InferenceCapability.SUMMARIZATION,
            inputText = """
                The company reported strong quarterly earnings, exceeding analyst expectations by 15 percent. Revenue growth was driven primarily by increased demand in the cloud services division, which saw a 28 percent year-over-year increase. The CEO attributed the success to strategic investments in infrastructure and talent acquisition over the past two years. However, the company faces increasing competition from emerging startups and established tech giants entering the market. Looking ahead, management expressed confidence in maintaining growth momentum through continued innovation and customer-focused product development.
            """.trimIndent(),
            category = TestCategory.MEDIUM_TEXT
        ),
        TestCase(
            id = "sum_casual_1",
            name = "Blog Post",
            capability = InferenceCapability.SUMMARIZATION,
            inputText = """
                I recently tried the new Italian restaurant downtown and I have to say, I was thoroughly impressed! The ambiance was cozy and welcoming, with soft lighting and beautiful decor. We started with the bruschetta which was absolutely delicious - fresh tomatoes, perfectly toasted bread, and just the right amount of garlic. For the main course, I ordered the carbonara and my partner got the lasagna. Both dishes were outstanding. The pasta was cooked al dente and the sauces were rich and flavorful. The service was attentive without being intrusive. Overall, it's definitely worth a visit if you're in the area!
            """.trimIndent(),
            category = TestCategory.CASUAL
        ),
        TestCase(
            id = "sum_short_1",
            name = "Short News",
            capability = InferenceCapability.SUMMARIZATION,
            inputText = """
                Local authorities announced new traffic regulations that will take effect next month. The changes include reduced speed limits in school zones and increased fines for violations. Residents have expressed mixed reactions to the announcement. School administrators welcomed the lower limits near campuses, while some commuters questioned whether the city had provided enough notice. Transportation officials said new signs will be installed before enforcement begins and that the first week will focus on public education. The city plans to review collision data after six months and adjust the measures if the evidence supports further changes.
            """.trimIndent(),
            category = TestCategory.SHORT_TEXT
        ),
        TestCase(
            id = "sum_long_1",
            name = "Research Summary",
            capability = InferenceCapability.SUMMARIZATION,
            inputText = """
                Recent studies in cognitive neuroscience have revealed new insights into how the human brain processes and retains information. Researchers at several leading universities have been investigating the neural mechanisms underlying memory formation and retrieval. Their findings suggest that the hippocampus plays a more complex role than previously understood. Using advanced imaging techniques, scientists have observed that different types of memories activate distinct neural pathways. Episodic memories, which involve personal experiences, show increased activity in the medial temporal lobe, while semantic memories, related to facts and concepts, engage broader cortical networks. The research also indicates that sleep plays a crucial role in memory consolidation, with specific sleep stages corresponding to different types of memory processing.

                One research group asked participants to learn associations between images, locations, and short factual descriptions. The participants returned for additional sessions after different sleep intervals. Imaging data suggested that newly learned associations initially relied on coordinated activity between the hippocampus and several cortical regions. Over time, some factual memories appeared to depend less heavily on the hippocampus, while detailed recollections of personal context continued to engage it. The researchers cautioned that the transition is gradual and varies across individuals. They also emphasized that laboratory tasks capture only a narrow portion of everyday memory.

                A second group examined how stress affected recall. Moderate stress shortly before learning produced mixed results, while sustained stress was associated with weaker performance on several retrieval tasks. Nutrition, exercise, and sleep regularity also appeared to influence outcomes, but the researchers warned against treating any single factor as a simple cause. The studies used different participant groups and measurement techniques, so direct comparisons require care. The interdisciplinary teams plan to continue their investigations with larger samples and longer follow-up periods.

                These discoveries could have significant implications for developing treatments for memory-related disorders and improving educational methods. However, the scientists stressed that the work remains exploratory. Brain imaging can reveal patterns of activity, but it does not automatically explain why a specific person remembers one event and forgets another. Future research will combine imaging, behavioral observations, and clinical evidence to test which findings translate into practical interventions.
            """.trimIndent(),
            category = TestCategory.LONG_TEXT
        )
    )

    val proofreadingTests = listOf(
        TestCase(
            id = "proof_grammar_1",
            name = "Grammar Errors",
            capability = InferenceCapability.PROOFREADING,
            inputText = "I goes to the store yesterday and buy some apple. Their very delicious!",
            category = TestCategory.ERROR_RICH
        ),
        TestCase(
            id = "proof_spelling_1",
            name = "Spelling Mistakes",
            capability = InferenceCapability.PROOFREADING,
            inputText = "The managment team has recieved several complains about the new polocies. We need to adress these isues immediatly.",
            category = TestCategory.ERROR_RICH
        ),
        TestCase(
            id = "proof_punctuation_1",
            name = "Punctuation Issues",
            capability = InferenceCapability.PROOFREADING,
            inputText = "hello how are you doing today i hope youre having a great day lets meet tomorrow at 3pm okay",
            category = TestCategory.ERROR_RICH
        ),
        TestCase(
            id = "proof_mixed_1",
            name = "Mixed Errors",
            capability = InferenceCapability.PROOFREADING,
            inputText = "The studens was working on there project when the teacher come in. She told them too finish it by tommorow.",
            category = TestCategory.ERROR_RICH
        ),
        TestCase(
            id = "proof_casual_1",
            name = "Casual Message",
            capability = InferenceCapability.PROOFREADING,
            inputText = "hey do u wanna grab lunch 2day? im thinking bout that new place on main st. lmk!",
            category = TestCategory.CASUAL
        )
    )

    val rewriteFormalTests = listOf(
        TestCase(
            id = "rewrite_formal_1",
            name = "Casual to Professional",
            capability = InferenceCapability.REWRITE_FORMAL,
            inputText = "Hey, I wanted to let you know that I can't make it to the meeting tomorrow. Something came up and I gotta deal with it. Sorry about that!",
            category = TestCategory.CASUAL
        ),
        TestCase(
            id = "rewrite_formal_2",
            name = "Email Response",
            capability = InferenceCapability.REWRITE_FORMAL,
            inputText = "Yeah, I got your message about the project deadline. No worries, I'll get it done by Friday. Thanks for the heads up!",
            category = TestCategory.CASUAL
        ),
        TestCase(
            id = "rewrite_formal_3",
            name = "Business Communication",
            capability = InferenceCapability.REWRITE_FORMAL,
            inputText = "So basically what we're trying to do here is make the app run faster. We think if we optimize the database stuff, things will work better.",
            category = TestCategory.TECHNICAL
        ),
        TestCase(
            id = "rewrite_formal_4",
            name = "Customer Service",
            capability = InferenceCapability.REWRITE_FORMAL,
            inputText = "Sorry about the delay! We had some issues on our end but we're working on fixing them. Your order should ship out soon.",
            category = TestCategory.CASUAL
        )
    )

    val rewriteCasualTests = listOf(
        TestCase(
            id = "rewrite_casual_1",
            name = "Formal to Friendly",
            capability = InferenceCapability.REWRITE_CASUAL,
            inputText = "I am writing to inform you that I will be unable to attend the scheduled meeting tomorrow due to a prior commitment. I apologize for any inconvenience this may cause.",
            category = TestCategory.FORMAL
        ),
        TestCase(
            id = "rewrite_casual_2",
            name = "Professional Email",
            capability = InferenceCapability.REWRITE_CASUAL,
            inputText = "Thank you for your inquiry regarding our services. We would be pleased to schedule a consultation at your earliest convenience to discuss your requirements in detail.",
            category = TestCategory.FORMAL
        ),
        TestCase(
            id = "rewrite_casual_3",
            name = "Technical Documentation",
            capability = InferenceCapability.REWRITE_CASUAL,
            inputText = "The system has been configured to automatically generate reports on a weekly basis. Users may access these documents through the administrative dashboard.",
            category = TestCategory.TECHNICAL
        ),
        TestCase(
            id = "rewrite_casual_4",
            name = "Announcement",
            capability = InferenceCapability.REWRITE_CASUAL,
            inputText = "Please be advised that the facility will be closed for maintenance on Saturday. We appreciate your understanding and cooperation in this matter.",
            category = TestCategory.FORMAL
        )
    )

    val rewriteConciseTests = listOf(
        TestCase(
            id = "rewrite_concise_1",
            name = "Verbose Email",
            capability = InferenceCapability.REWRITE_CONCISE,
            inputText = "I wanted to take a moment to reach out and let you know that I've been giving some thought to your proposal, and after careful consideration and discussion with my team, I believe we should move forward with the project.",
            category = TestCategory.FORMAL
        ),
        TestCase(
            id = "rewrite_concise_2",
            name = "Long Explanation",
            capability = InferenceCapability.REWRITE_CONCISE,
            inputText = "In order to complete the registration process, you will need to fill out all of the required fields in the form, including your name, email address, and phone number, and then click on the submit button at the bottom of the page.",
            category = TestCategory.TECHNICAL
        ),
        TestCase(
            id = "rewrite_concise_3",
            name = "Wordy Instructions",
            capability = InferenceCapability.REWRITE_CONCISE,
            inputText = "If you happen to experience any kind of issues or problems while you are using the application, please don't hesitate to contact our support team who will be more than happy to assist you.",
            category = TestCategory.FORMAL
        ),
        TestCase(
            id = "rewrite_concise_4",
            name = "Redundant Message",
            capability = InferenceCapability.REWRITE_CONCISE,
            inputText = "At this point in time, we are currently in the process of reviewing and evaluating all of the different options and alternatives that are available to us.",
            category = TestCategory.GENERAL
        )
    )

    fun getTestCases(capability: InferenceCapability): List<TestCase> {
        return when (capability) {
            InferenceCapability.SUMMARIZATION -> summarizationTests
            InferenceCapability.PROOFREADING -> proofreadingTests
            InferenceCapability.REWRITE_FORMAL -> rewriteFormalTests
            InferenceCapability.REWRITE_CASUAL -> rewriteCasualTests
            InferenceCapability.REWRITE_CONCISE -> rewriteConciseTests
        }
    }

    fun getAllTestCases(): List<TestCase> {
        return summarizationTests + proofreadingTests + rewriteFormalTests + rewriteCasualTests + rewriteConciseTests
    }
}
