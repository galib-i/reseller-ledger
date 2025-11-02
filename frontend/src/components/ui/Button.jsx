import { forwardRef, Children, cloneElement } from 'react';

const cx = (...classes) => classes.filter(Boolean).join(' ');

const Slot = forwardRef(({ children, ...props }, ref) => {
  const child = Children.only(children);
  return cloneElement(child, { ...props, ref });
});
Slot.displayName = 'Slot';

const buttonStyles = {
  base: 'inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-sm text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:pointer-events-none disabled:opacity-50 [&_svg]:pointer-events-none [&_svg]:size-4 [&_svg]:shrink-0 cursor-pointer',
  variants: {
    default: 'bg-primary text-primary-foreground hover:bg-primary/90',
    outline: `border border-input bg-background shadow-sm hover:bg-accent
      hover:text-accent-foreground`,
  },
  sizes: {
    default: 'h-9 px-4 py-2',
    sm: 'h-8 rounded-md px-3 text-xs',
    lg: 'h-10 rounded-md px-8',
    icon: 'h-9 w-9',
  },
};

const Button = forwardRef(
  (
    {
      className,
      variant = 'default',
      size = 'default',
      asChild = false,
      'aria-label': ariaLabel,
      ...props
    },
    ref
  ) => {
    const Comp = asChild ? Slot : 'button';

    const classes = cx(
      buttonStyles.base,
      buttonStyles.variants[variant] || buttonStyles.variants.default,
      buttonStyles.sizes[size] || buttonStyles.sizes.default,
      className
    );

    return <Comp className={classes} ref={ref} aria-label={ariaLabel} {...props} />;
  }
);

Button.displayName = 'Button';

export { Button };
