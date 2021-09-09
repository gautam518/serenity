package com.loyaltyprimemultipartnerapp.cucumber;

import org.junit.runner.RunWith;

import com.loyaltyprimemultipartnerapp.testbase.TestBase;

import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;

@RunWith(CucumberWithSerenity.class)

@CucumberOptions(plugin = { "pretty" }, features =
//{"src/test/resources/features/api/TransactionAPI/02_TransactionLop.feature"}
//{"src/test/resources/features/api/IntegrationTest/4_TransactionIntegration.feature"}
//features = "src/test/resources/features/api/TransactionAPI"
//# if need to run the automation for below 5.1 version then pls un commnent this line and run
//{"src/test/resources/features/api"},tags = {"not @PartialTest","not @NewVersion","not @membershipstatus","not @current","not @tier"}
//# if need to run the automation for above 5.1 version then pls un commnent this line and run
//{"src/test/resources/features/api"},tags = {"not @PartialTest","not @current","not @membershipstatus"} //for new version

//{"src/test/resources/features/api/TransactionAPI/04_TransactionReversalLop.feature"},tags = {"not @PartialTest","not @NewVersion"}
//{"src/test/resources/features/api"},tags = {"not @PartialTest","not @NewVersion,not @ignore"}
//{"src/test/resources/features/api/TransactionAPI/01_PointAdjustmentLop.feature"}
//{"src/test/resources/features/api/Miscellaneous/MemberNegativeBalanceTransactions.feature"}
//{"src/test/resources/features/api/PartialTransaction/PartialTransaction.feature"}
//{"src/test/resources/features/api/MemberMerge/MemberMerge.feature"},tags = {"not @NewVersion"} 
//{"src/test/resources/features/api/PointTransfer/MemberPointExpiry.feature"}

 //{ "src/test/resources/features/api/Miscellaneous/ExportLop.feature"},tags = {"@current" }
//{ "src/test/resources/features/api/Miscellaneous/MemberTierAssessmentLop.feature"},tags = {"@current" }
//{ "src/test/resources/features/api/Enrollment/1_MemberEnroll.feature"}
//{ "src/test/resources/features/api/MemberMerge/MemberMerge.feature"}
//{ "src/test/resources/features/api//IntegrationTest//5_PointExpiryIntegration.feature"}
//{ "src/test/resources/features/api/Enrollment/FullMembershipImport.feature"}
//{ "src/test/resources/features/api/Miscellaneous/Currency.feature"}
//
//{ "src/test/resources/features/api/PointTransfer/MemberPointExpiry.feature"}
//{ "src/test/resources/features/api/Miscellaneous/ImportLop.feature"},tags = {"not @NewVersion" }


//{ "src/test/resources/features/api/Miscellaneous/EventIntegration.feature"},tags = {"not @NewVersion" }
//
//{ "src/test/resources/features/api/Miscellaneous/MemberEstimatedExpiration.feature"},tags = {"not @NewVersion" }
//
//{ "src/test/resources/features/api/Miscellaneous/VoucherIssueDateOnSinceDate.feature"},tags = {"not @NewVersion" }
//
//{ "src/test/resources/features/api/Miscellaneous/PointTransferAccountIds.feature"},tags = {"not @NewVersion" }
//{ "src/test/resources/features/api/Miscellaneous/BulkImportPointTransfer.feature"},tags = {"not @NewVersion" }

//BalanceAPI.feature
//{ "src/test/resources/features/api/Miscellaneous/BalanceAPI.feature"},tags = {"not @NewVersion" }
//.feature
//{ "src/test/resources/features/api/Miscellaneous/MembershipExistsApi.feature"},tags = {"not @NewVersion" }
//
//{ "src/test/resources/features/api/Miscellaneous/TierAssessmentLogEvent.feature"},tags = {"not @NewVersion" }

{ "src/test/resources/features/api/Miscellaneous/BulkImportProducts.feature"},tags = {"not @NewVersion" }

)
public class TestRunner extends TestBase {
	
}
