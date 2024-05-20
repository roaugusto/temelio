import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
} from '@mui/material'

export interface SimpleDialogProps {
  title: string
  description: string
  description2?: string
  open: boolean
  onClose: (value: boolean) => void
  btnCancel?: string
  btnOk: string
  isWarning?: boolean
}

export function DialogMessage({
  onClose,
  open,
  title,
  description,
  description2,
  btnCancel,
  btnOk,
  isWarning = false,
}: SimpleDialogProps) {
  const handleClose = (value: boolean) => {
    onClose(value)
  }

  return (
    <Dialog
      open={open}
      onClose={handleClose}
      aria-labelledby="alert-dialog-title"
      aria-describedby="alert-dialog-description"
      fullWidth
    >
      <DialogTitle
        className="bg-blue-100 text-custom-secondary"
        id="alert-dialog-title"
      >
        {title}
      </DialogTitle>
      <DialogContent style={{ marginTop: 24, marginBottom: 10 }}>
        <DialogContentText
          id="alert-dialog-description"
          className="text-blue-900"
          style={{ fontWeight: 600 }}
        >
          {description}
        </DialogContentText>
        <DialogContentText
          id="alert-dialog-description"
          style={{ marginTop: 8 }}
        >
          {description2}
        </DialogContentText>
      </DialogContent>
      <DialogActions className="bg-blue-25" style={{ padding: 15 }}>
        {btnCancel && (
          <Button
            variant="outlined"
            color="inherit"
            onClick={() => handleClose(false)}
          >
            {btnCancel}
          </Button>
        )}
        <Button
          variant="contained"
          // color="error"
          style={{ backgroundColor: isWarning ? '#6d6ca2' : '#BA0517' }}
          onClick={() => handleClose(true)}
          autoFocus
        >
          {btnOk}
        </Button>
      </DialogActions>
    </Dialog>
  )
}
