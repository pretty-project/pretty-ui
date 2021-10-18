
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.18
; Description:
; Version: v0.2.0
; Compatibility: x4.4.1



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.user-handler
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :as map]
              [x.server-core.api :as a]))



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- user-props->user-account
  ; @return (map)
  [user-props]
  (map/inherit user-props [:email-address :password :pin :roles]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- user-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) user-props
  ;  {:roles (vector)}
  [{:keys [roles] :as user-props}]
  (merge (param user-props)
         {:roles []}))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.server-user/add-user!
  ; @param (map) user-props
  ;  {:email-address (string)(opt)
  ;   :password (string)(opt)
  ;   :pin (string)(opt)
  ;   :roles (strings in vector)(opt)}
  ;
  ; @usage
  ;  [:x.server-user/add-user {...}]
  ;
  ; @usage
  ;  [:x.server-user/add-user :my-user {...}]
  (fn [_ [_ user-props]]
      (let [user-props (a/prot user-props user-props-prototype)])))
