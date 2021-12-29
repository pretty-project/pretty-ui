
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.17
; Description:
; Version: v0.5.2
; Compatibility: x4.4.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.session-handler
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.string    :as string]
              [mid-fruits.time      :as time]
              [mid-fruits.vector    :as vector]
              [server-fruits.http   :as http]
              [x.server-user.engine :as engine]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
;  {:user-account/id (nil)
;   :user-account/roles (vector)}
(def ANONYMOUS-SESSION {:user-account/id    nil
                        :user-account/roles []})



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn session->session-valid?
  ; @param (map) session
  ;  {:user-account/id (string)(opt)
  ;   :user-account/roles (vector)(opt)}
  ;
  ; @return (boolean)
  [{:user-account/keys [id roles]}]
  (and (string/nonempty? id)
       (vector/nonempty? roles)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! DEPRECATED! DO NOT USE!
(defn request->add-props
  ; @param (map) request
  ;
  ; @return (map)
  ;  {:modified-at (object)
  ;   :modified-by (map)}
  [request]
  (let [account-id   (http/request->session-param request :user-account/id)
        user-account {:user-account/id account-id}
        timestamp    (time/timestamp-object)]
       {:added-at timestamp
        :added-by user-account}))
; WARNING! DEPRECATED! DO NOT USE!

; WARNING! DEPRECATED! DO NOT USE!
(defn request->modify-props
  ; @param (map) request
  ;
  ; @return (map)
  ;  {:modified-at (object)
  ;   :modified-by (map)}
  [request]
  (let [account-id   (http/request->session-param request :user-account/id)
        user-account {:user-account/id account-id}
        timestamp    (time/timestamp-object)]
       {:modified-at timestamp
        :modified-by user-account}))
; WARNING! DEPRECATED! DO NOT USE!

; WARNING! DEPRECATED! DO NOT USE!
(defn request->create-props
  ; @param (map) request
  ;
  ; @return (map)
  ;  {:created-at (object)
  ;   :created-by (map)}
  [request]
  (let [account-id   (http/request->session-param request :user-account/id)
        user-account {:user-account/id account-id}
        timestamp    (time/timestamp-object)]
       {:created-at timestamp
        :created-by user-account}))
; WARNING! DEPRECATED! DO NOT USE!

; WARNING! DEPRECATED! DO NOT USE!
(defn request->delete-props
  ; @param (map) request
  ;
  ; @return (map)
  ;  {:deleted-at (object)
  ;   :deleted-by (map)}
  [request]
  (let [account-id   (http/request->session-param request :user-account/id)
        user-account {:user-account/id account-id}
        timestamp    (time/timestamp-object)]
       {:deleted-at timestamp
        :deleted-by user-account}))
; WARNING! DEPRECATED! DO NOT USE!

; WARNING! DEPRECATED! DO NOT USE!
(defn request->upload-props
  ; @param (map) request
  ;
  ; @return (map)
  ;  {:uploaded-at (object)
  ;   :uploaded-by (map)}
  [request]
  (let [account-id   (http/request->session-param request :user-account/id)
        user-account {:user-account/id account-id}
        timestamp    (time/timestamp-object)]
       {:uploaded-at timestamp
        :uploaded-by user-account}))
; WARNING! DEPRECATED! DO NOT USE!