
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.16
; Description:
; Version: v0.3.8
; Compatibility: x4.5.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.debug-handler
    (:require [app-fruits.window        :as window]
              [mid-fruits.candy         :refer [param return]]
              [mid-fruits.uri           :as uri]
              [re-frame.core            :as re-frame]
              [x.mid-core.debug-handler :as debug-handler]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Az x.app-core.debug-handler névtérben a re-frame.core névtér használatával lehetséges Re-Frame
; eseményeket regisztrálni, mert az x.app-core.debug-handler és x.app-core.event-handler névterek
; körkörös függőségben vannak.



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def query-string->debug-mode debug-handler/query-string->debug-mode)



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

(defn log-events?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db]
  (= "pineapple-juice" (get-debug-mode db nil)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- set-debug-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) debug-mode
  ;
  ; @return (map)
  [db [_ debug-mode]]
  (assoc-in db [::primary :meta-items :debug-mode] debug-mode))

(re-frame/reg-event-db :core/set-debug-mode! set-debug-mode!)



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- detect-debug-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [uri          (window/get-uri)
        query-string (uri/uri->query-string uri)]
       (re-frame/dispatch [:db/set-item! [::primary :meta-items :debug-mode]
                                         (query-string->debug-mode query-string)])))

(re-frame/reg-fx :core/detect-debug-mode! detect-debug-mode!)
