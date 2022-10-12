
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.title.subs
    (:require [mid-fruits.candy     :refer [return]]
              [mid-fruits.string    :as string]
              [re-frame.api         :refer [r]]
              [x.app-components.api :as components]
              [x.app-core.api       :as core]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-window-title-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (metamorphic-content) window-title
  ;
  ; @return (string)
  [db [_ window-title]]
  (let [app-title    (r core/get-app-config-item         db :app-title)
        window-title (r components/get-metamorphic-value db window-title)
        window-title (string/trim window-title)]
       (if (empty? window-title)
           (return app-title)
           (str    window-title " - " app-title))))
