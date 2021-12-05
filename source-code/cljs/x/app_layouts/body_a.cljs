
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.04
; Description:
; Version: v0.2.8
; Compatibility: x4.4.7



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.body-a
    (:require [x.app-core.api     :as a]
              [x.app-elements.api :as elements]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body-a
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword)(opt) body-id
  ; @param (map) body-props
  ;
  ; @return (hiccup)
  [body-id body-props]
  [:div.x-body-a])

(defn body
  ; @param (keyword)(opt) body-id
  ; @param (map) body-props
  ;
  ; @return (component)
  ([body-props]
   [body (a/id) body-props])

  ([body-id body-props]
   [body body-id body-props]))
