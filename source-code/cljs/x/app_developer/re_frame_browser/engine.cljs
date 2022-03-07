
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.re-frame-browser.engine
    (:require [mid-fruits.vector :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keywords in vector)
(def HIDDEN-ITEMS [:core :developer :dictionary :elements :environment :locales :router :sync :tools :user :ui])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn map-item-hidden?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [n {:keys [root-level?]}]
  (or (and root-level? (-> n namespace some?))
      (vector/contains-item? HIDDEN-ITEMS n)))

(defn render-map-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [n {:keys [show-hidden?] :as body-props}]
  (or show-hidden? (not (map-item-hidden? n body-props))))
