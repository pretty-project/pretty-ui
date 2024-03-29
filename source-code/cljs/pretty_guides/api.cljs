
(ns pretty-guides.api
    (:require [pretty-guides.error-text.views       :as error-text.views]
              [pretty-guides.helper-text.views      :as helper-text.views]
              [pretty-guides.info-text.views        :as info-text.views]
              [pretty-guides.placeholder-text.views :as placeholder-text.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (*/view)
(def error-text       error-text.views/view)
(def helper-text      helper-text.views/view)
(def info-text        info-text.views/view)
(def placeholder-text placeholder-text.views/view)
