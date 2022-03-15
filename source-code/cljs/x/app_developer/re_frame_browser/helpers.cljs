
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.re-frame-browser.helpers
    (:require [mid-fruits.vector                       :as vector]
              [x.app-developer.re-frame-browser.config :as re-frame-browser.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn map-item-hidden?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [n]
  (or (vector/contains-item? re-frame-browser.config/HIDDEN-ITEMS n)

      ; TEMP
      ; Ha már egyik modul sem használ névteres kulcsszót, mint ...
      (and (keyword? n) (-> n namespace some?))))
