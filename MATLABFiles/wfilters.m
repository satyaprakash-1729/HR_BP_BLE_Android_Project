function [a, b, H, G] = wfilters (wname, type)
  if (type=="d")
  end
  if (nargin == 0 || nargin > 2)
    print_usage ();
  end
  Hdb = [[0.2303778133088552 0.7148465705525415 0.6308807679295904 -0.02798376941698385 -0.1870348117188811 0.03084138183598696 0.03288301166698294 -0.01059740178499728];[0.2303778133088552 0.7148465705525415 0.6308807679295904 -0.02798376941698385 -0.1870348117188811 0.03084138183598696 0.03288301166698294 -0.01059740178499728];[0.2303778133088552 0.7148465705525415 0.6308807679295904 -0.02798376941698385 -0.1870348117188811 0.03084138183598696 0.03288301166698294 -0.01059740178499728]];
    
  if (strncmpi (wname, "db", 2))
    wn = extractAfter(wname, 2);
    p = str2double (wn);
    H = Hdb(p);
    G = fliplr (H);
    G(2:2:end) = -G(2:2:end);
  else  
    print_usage ();
  end

  a = fliplr (H);
  b = fliplr (G);

%{
  persistent coif = {
  [-0.051429728471 0.238929728471 0.602859456942 0.272140543058 -0.051429728471 -0.011070271529];
  [0.011587596739 -0.02932013798 -0.04763959031 0.273021046535 0.574682393857 0.294867193696 -0.054085607092 -0.042026480461 0.016744410163 0.003967883613 -0.001289203356 -0.000509505399];
  [-0.002682418671 0.005503126709 0.016583560479 -0.046507764479 -0.04322076356 0.286503335274 0.56128525687 0.302983571773 -0.050770140755 -0.058196250762 0.024434094321 0.011229240962 -0.006369601011 -0.001820458916 0.000790205101 0.000329665174 -5.0192775e-05 -2.4465734e-05];
  [0.000630961046 -0.001152224852 -0.005194524026 0.011362459244 0.018867235378 -0.057464234429 -0.039652648517 0.293667390895 0.553126452562 0.307157326198 -0.047112738865 -0.068038127051 0.027813640153 0.017735837438 -0.010756318517 -0.004001012886 0.002652665946 0.000895594529 -0.000416500571 -0.000183829769 4.4080354e-05 2.2082857e-05 -2.304942e-06 -1.262175e-06];
  [-0.0001499638 0.0002535612 0.0015402457 -0.0029411108 -0.0071637819 0.0165520664 0.0199178043 -0.0649972628 -0.0368000736 0.2980923235 0.5475054294 0.309706849 -0.0438660508 -0.0746522389 0.0291958795 0.023110777 -0.0139736879 -0.00648009 0.0047830014 0.0017206547 -0.0011758222 -0.000451227 0.0002137298 9.93776e-05 -2.92321e-05 -1.5072e-05 2.6408e-06 1.4593e-06 -1.184e-07 -6.73e-08]};
coif


  persistent sym = {
  [];
  [0.341506350946439 0.5915063509462454 0.1584936490538809 -0.09150635094592553];
  [0.2352336038927046 0.5705584579173084 0.3251825002637103 -0.0954672077842601 -0.06041610415535419 0.02490874986589096];
  [0.022785172948 -0.008912350720850001 -0.07015881208950001 0.210617267102 0.5683291217050001 0.351869534328 -0.02095548256255 -0.053574450709];
  [0.01381607647892832 -0.01492124993438059 -0.1239756813067542 0.01173946156807407 0.448290824190919 0.5115264834460486 0.1409953484272891 -0.02767209305835673 0.02087343221079279 0.01932739797743962];
  [-0.005515933754689684 0.001249961046392662 0.03162528132994086 -0.01489187564922215 -0.05136248493090395 0.2389521856660531 0.5569463919639585 0.3472289864783507 -0.03416156079323612 -0.08343160770584397 0.002468306185920385 0.01089235016327963];
  [0.007260697381013287 0.002835671342875189 -0.0762319359481395 -0.09902835340368121 0.2040919698628735 0.5428913549059935 0.3790813009826937 0.01233282974432257 -0.03503914561106412 0.04800738396783785 0.02157772629103949 -0.008935215825566351 -0.0007406129573013554 0.00189632926710347];
  [0.001336396696402591 -0.0002141971501226139 -0.01057284326418094 0.002693194376880145 0.03474523295558737 -0.0192467606316653 -0.03673125438038378 0.2576993351865354 0.5495533152690092 0.3403726735943864 -0.04332680770282189 -0.1013243276428173 0.005379305875240302 0.0224118115218096 -0.0003833454481133089 -0.002391729255745689]};
sym
%}

  end