
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.re-frame-browser.helpers
    (:require [mid-fruits.vector :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keywords in vector)
(def HIDDEN-ITEMS [:core :developer :dictionary :elements :environment :locales :router :sync :tools :user :ui])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn map-item-hidden?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [n]
  (or (-> n namespace some?)
      (vector/contains-item? HIDDEN-ITEMS n)))
