'use client'

import { AlertCircle } from 'lucide-react'
import { ComponentProps, useState } from 'react'
import { useFormContext } from 'react-hook-form'

type TextAreaRootProps = ComponentProps<'div'> & {
  disabled?: boolean
}

type TextAreaPrefixProps = ComponentProps<'div'>
type TextAreaControlProps = ComponentProps<'textarea'> & {
  name: string
  hasError?: boolean
  errorMessage?: string
}

export function TextAreaPrefix(props: TextAreaPrefixProps) {
  return <div {...props} />
}


export function TextAreaControl({
  hasError = false,
  errorMessage = 'O campo possui erros',
  name,
  ...props
}: TextAreaControlProps) {
  const methods = useFormContext()
  const [showTooltip, setShowTooltip] = useState(false)

  const handleMouseEnter = () => setShowTooltip(true)
  const handleMouseLeave = () => setShowTooltip(false)


  return (
    <>
      <textarea
        {...props}
        id={name}
        className={`w-full max-w-full flex-1 border-0 bg-transparent p-0 outline-none text-blue-900 placeholder-blue-300`}
        autoComplete="new-password"
        {...methods?.register(name)}
      />
      {hasError && (
        <div className="relative flex items-center">
          <div onMouseEnter={handleMouseEnter} onMouseLeave={handleMouseLeave}>
            <AlertCircle className="h-5 w-5 text-red-700" />
            {showTooltip && (
              <div
                className="absolute bottom-full mb-3 w-34 rounded-lg bg-red-700 px-4 py-2 text-sm text-white shadow-md"
                style={{
                  left: '50%',
                  transform: 'translateX(-50%)',
                }}
              >
                {errorMessage}
                <div
                  className="absolute left-1/2 h-3 w-3 -translate-x-1/2 -translate-y-1/2 rotate-45 transform bg-red-700"
                  style={{ bottom: '-10px' }}
                ></div>
              </div>
            )}
          </div>
        </div>
      )}
    </>
  )
}

export function TextAreaRoot({
  disabled = false,
  ...props
}: TextAreaRootProps) {

  return (
    <div
      className={`mx-1 flex w-full items-center gap-2 rounded-lg border px-3 py-2 shadow-sm  focus-within:ring-4 border-blue-100 focus-within:ring-blue-100 focus-within:border-blue-300
      ${disabled && 'bg-zinc-50'} `}
      {...props}
    ></div>
  )
}
