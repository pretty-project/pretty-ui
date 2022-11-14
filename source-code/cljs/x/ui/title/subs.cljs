
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.title.subs
    (:require [candy.api         :refer [return]]
              [mid-fruits.string :as string]
              [re-frame.api      :refer [r]]
              [x.components.api  :as x.components]
              [x.core.api        :as x.core]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-tab-title-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (metamorphic-content) tab-title
  ;
  ; @return (string)
  [db [_ tab-title]]
  (let [app-title (r x.core/get-app-config-item         db :app-title)
        tab-title (r x.components/get-metamorphic-value db tab-title)
        tab-title (string/trim tab-title)]
       (if (empty? tab-title)
           (return app-title)
           (str    tab-title " - " app-title))))
