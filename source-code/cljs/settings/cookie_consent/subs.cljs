

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns settings.cookie-consent.subs
    (:require [x.app-core.api        :as a :refer [r]]
              [x.app-environment.api :as environment]
              [x.app-ui.api          :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn render-consent?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (and      (r ui/application-interface?              db)
       (not (r environment/necessary-cookies-enabled? db))))
