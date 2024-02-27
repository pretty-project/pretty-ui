
(ns pretty-guides.api
    (:require [pretty-guides.helper-text.views :as helper-text.views]
              [pretty-guides.info-text.views   :as info-text.views]
              [pretty-guides.error-text.views  :as error-text.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (*/view)
(def error-text  error-text.views/view)
(def helper-text helper-text.views/view)
(def info-text   info-text.views/view)
