

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.re-frame-browser.subs
    (:require [mid-fruits.mixed :as mixed]
              [x.app-core.api   :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-meta-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ item-key]]
  (get-in db [:developer :re-frame-browser/meta-items item-key]))

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
(a/reg-sub :re-frame-browser/get-meta-item get-meta-item)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :re-frame-browser/get-current-path get-current-path)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :re-frame-browser/get-current-item get-current-item)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :re-frame-browser/root-level? root-level?)
