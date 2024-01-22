
(ns pretty-forms.api
    (:require [pretty-forms.env          :as env]
              [pretty-forms.side-effects :as side-effects]
              [pretty-forms.state        :as state]
              [pretty-forms.views        :as views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (pretty-forms.env/*)
(def get-input-value             env/get-input-value)
(def get-input-validators        env/get-input-validators)
(def get-input-invalid-message   env/get-input-invalid-message)
(def get-form-inputs             env/get-form-inputs)
(def get-input-validation-result env/get-input-validation-result)

; @redirect (pretty-forms.side-effects/*)
(def reg-form-input!     side-effects/reg-form-input!)
(def dereg-form-input!   side-effects/dereg-form-input!)
(def autovalidate-input! side-effects/autovalidate-input!)
(def autovalidate-form!  side-effects/autovalidate-form!)
(def validate-input!     side-effects/validate-input!)
(def validate-form!      side-effects/validate-form!)
(def input-changed       side-effects/input-changed)
(def input-left          side-effects/input-left)

; @redirect (pretty-forms.state/*)
(def FORM-INPUTS state/FORM-INPUTS)
(def FORM-ERRORS state/FORM-ERRORS)

; @redirect (pretty-forms.views/*)
(def invalid-message views/invalid-message)
