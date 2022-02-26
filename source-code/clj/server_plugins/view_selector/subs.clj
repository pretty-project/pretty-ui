
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.view-selector.subs
    (:require [mid-plugins.item-lister.subs :as subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.view-selector.subs
(def get-selector-props subs/get-lister-props)
(def get-meta-item      subs/get-meta-item)
