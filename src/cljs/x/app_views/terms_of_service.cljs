
; PDF formátumú letölthető fájl lesz ez a view helyett



;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.11
; Description:
; Version: v0.1.0
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.terms-of-service
    (:require [x.app-core.api     :as a]
              [x.app-elements.api :as elements]
              [x.app-ui.api       :as ui]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (hiccup)
  []
  [elements/box {:content "Coming soon..."}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/set-surface!
   ::view
   {:content #'view
    :label-bar {:content #'ui/go-back-surface-label-bar
                :content-props {:label :terms-of-service}}}])

(a/reg-event-fx
  ::initialize!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-router/add-route!
   ::route
   {:route-event    [::render!]
    :route-template "/terms-of-service"}])

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [::initialize!]})
