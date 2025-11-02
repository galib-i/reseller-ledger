import { forwardRef, useState } from 'react';

const cx = (...classes) => classes.filter(Boolean).join(' ');

const Input = forwardRef(({ className, type, placeholder, value, onChange, id, ...props }, ref) => {
  const [isFocused, setIsFocused] = useState(false);
  const [hasValue, setHasValue] = useState(Boolean(value));

  return (
    <div className="relative w-full">
      <input
        type={type}
        className={cx(
          'peer flex h-12 w-full rounded-sm border px-3 py-1 text-base shadow-sm transition-colors focus-visible:border-0 focus-visible:ring-1 focus-visible:outline-none disabled:cursor-not-allowed disabled:opacity-50 md:text-sm',
          className,
          isFocused ? 'border-blue-500' : ''
        )}
        ref={ref}
        onFocus={() => setIsFocused(true)}
        onBlur={() => setIsFocused(false)}
        onChange={(e) => {
          setHasValue(e.target.value.length > 0);
          onChange?.(e);
        }}
        value={value}
        aria-label={placeholder}
        id={id}
        {...props}
      />
      <label
        htmlFor={id}
        className={cx(
          'absolute top-1/2 left-3 -translate-y-1/2 transform text-sm text-zinc-500 transition-all duration-200 ease-in-out',
          isFocused || hasValue ? '-translate-y-6 text-xs text-white' : ''
        )}
        style={{ pointerEvents: 'none' }}
      >
        {placeholder}
      </label>
    </div>
  );
});

Input.displayName = 'Input';

export { Input };
