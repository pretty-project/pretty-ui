
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.input.views
    (:require [mid-fruits.candy      :refer [param]]
              [x.app-components.api  :as components]
              [x.app-core.api        :as a]
              [x.app-environment.api :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn invalid-message
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {}
  ;
  ; @return (metamorphic-content)
  [input-id {:keys [required? validator] :as input-props}])
