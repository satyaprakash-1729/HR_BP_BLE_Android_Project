function y = wconv (type, x, f, shape)
  if(nargin==2)
      shape = "full";
  else
  if (nargin < 3 || nargin > 4)
    print_usage ();
  end
  end
  switch (type)
    case {1, "1", "1d", "1D"}
      y = conv2 (x(:).', f(:).', shape);
      if (size (x,1) > 1)
        y = y.';
      end
    case {2, "2", "2d", "2D"}
      y = conv2 (x, f, shape);
    case {"r", "row"}
      y = conv2 (x, f(:).', shape);
    case {"c", "col"}
      y = conv2 (x.', f(:).', shape);
      y = y.';
    otherwise
      print_usage ();
  end
end