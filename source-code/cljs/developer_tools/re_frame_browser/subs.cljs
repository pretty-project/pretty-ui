
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.re-frame-browser.subs
    (:require [mixed.api    :as mixed]
              [re-frame.api :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-meta-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ item-key]]
  (get-in db [:developer-tools :re-frame-browser/meta-items item-key]))

(defn get-current-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [current-path (r get-meta-item db :current-path)]
       (mixed/to-vector current-path)))

(defn get-current-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [current-path (r get-current-path db)]
       (get-in db current-path)))

(defn root-level?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [current-path (r get-current-path db)]
       (= current-path [])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :developer-tools.re-frame-browser/get-meta-item get-meta-item)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :developer-tools.re-frame-browser/get-current-path get-current-path)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :developer-tools.re-frame-browser/get-current-item get-current-item)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :developer-tools.re-frame-browser/root-level? root-level?)
