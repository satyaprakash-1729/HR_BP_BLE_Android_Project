# HR_BP_BLE_Android_Project
Heart Rate &amp; Blood Pressure Monitoring System
## ABSTRACT

The fields of Medical Sciences & Computer Sciences & Electronics are as intermingled as possible in current day scenario, but the real challenge is integrating the technology into devices so as to make them easily usable and understandable by as large a populace as possible. This project aims at the development of an Android Application that is easy to handle and is usable by anyone with basic knowledge of Android Usage and thus provides a sophisticated method to get crucial data including heart rate & Blood Pressure even for common people.

The tools/instruments used for the development of the project are listed below:-
- CC2650EMK-7ID
- SmartRF06 Evaluation Board
- Android Device

## Snapshots

<img src="Images/ECG.png" height="500" width="300" alt="ECG Signal">    <img src="Images/PPG.png" height="500" width="300" alt="PPG Signal 1">
<center><img src="Images/PPG1.png" height="500" width="300" alt="PPG signal 2"></center>

## MATLAB Code
**To train the model :**
1. Run : [SFeatureMatrix DFeatureMatrix] = getMatrix();
2. Take anyone let's say SFeatureMatrix then
	X = SFeatureMatrix(:,1:10);
	Y = SFeatureMatrix(:,11);
	m = length(Y);
	X = [ones(m, 1) X];
3. Follow these steps or go to step 4
	alpha = 0.01;
	num_iters = 1000;
	theta = zeros(11, 1);
	[theta, J_history] = gradientDescentMulti(X, y, theta, alpha, num_iters);
4. theta = pinv(X' * X) * X' * Y;
5. We get the desired theta.
