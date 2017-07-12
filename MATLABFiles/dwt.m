function [u, v] = dwt (x, varargin)

  if (nargin == 2)
    wname = varargin{1};
    [Hp, Gp, ~, ~] = wfilters (wname, "d");
  elseif (nargin == 3)
    Hp = varargin{1};
    Gp = varargin{2};
  else
    print_usage ();
  end

  tmp = wconv (1, x, Hp, "valid");
  u = tmp(1:2:end);
  
  tmp = wconv (1, x, Gp, "valid");
  v = tmp(1:2:end);

end