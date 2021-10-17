
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.16
; Description:
; Version: v0.2.0
; Compatibility: x4.4.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.debug-handler
    (:require [app-fruits.window        :as window]
              [mid-fruits.uri           :as uri]
              [x.mid-core.debug-handler :as debug-handler]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Az x.app-core.debug-handler névtérben nem lehetséges Re-Frame eseményeket regisztrálni
; Az x.app-core.debug-handler és x.app-core.event-handler névterek körkörös függőségben vannak.



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def query-string->debug-mode? debug-handler/query-string->debug-mode?)
(def query-string->debug-mode  debug-handler/query-string->debug-mode)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn debug-mode?
  ; @return (boolean)
  []
  (let [uri          (window/get-uri)
        query-string (uri/uri->query-string uri)]
       (query-string->debug-mode? query-string)))

(defn debug-mode
  ; @return (string)
  []
  (let [uri          (window/get-uri)
        query-string (uri/uri->query-string uri)]
       (query-string->debug-mode query-string)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-debug-mode
  ; @return (string)
  [db _]
  (get-in db [::primary :meta-items :debug-mode]))

(defn debug-mode-detected?
  ; @return (boolean)
  [db [event-id]]
  (let [debug-mode (get-debug-mode db [event-id])]
       (some? debug-mode)))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-debug-mode!
  ; @param (string) debug-mode
  ;
  ; @return (map)
  [db [_ debug-mode]]
  (assoc-in db [::primary :meta-items :debug-mode]
            debug-mode))
