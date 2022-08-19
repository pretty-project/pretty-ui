
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.re-frame-browser.events
    (:require [mid-fruits.map                        :refer [dissoc-in]]
              [mid-fruits.pretty                     :as pretty]
              [mid-fruits.reader                     :as reader]
              [mid-fruits.vector                     :as vector]
              [x.app-core.api                        :as a :refer [r]]
              [x.app-developer.re-frame-browser.subs :as re-frame-browser.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn go-to!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ item-path]]
  (-> db (dissoc-in [:developer :re-frame-browser/meta-items])
         (assoc-in  [:developer :re-frame-browser/meta-items :current-path] item-path)))

(defn go-up!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [current-path (r re-frame-browser.subs/get-current-path db)
        parent-path  (vector/remove-last-item current-path)]
       (r go-to! db parent-path)))

(defn toggle-subscription!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (if-let [subscribe? (get-in db [:developer :re-frame-browser/meta-items :subscribe?])]
          (assoc-in db [:developer :re-frame-browser/meta-items :subscribe?] false)
          (assoc-in db [:developer :re-frame-browser/meta-items :subscribe?] true)))

(defn toggle-data-view!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (if-let [show-original? (get-in db [:developer :re-frame-browser/meta-items :show-data?])]
          (assoc-in db [:developer :re-frame-browser/meta-items :show-data?] false)
          (assoc-in db [:developer :re-frame-browser/meta-items :show-data?] true)))

(defn toggle-edit-item-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [current-path (r re-frame-browser.subs/get-current-path db)]
       (if-let [edit-item? (r re-frame-browser.subs/get-meta-item db :edit-item?)]
               (let [edited-item (r re-frame-browser.subs/get-meta-item db :edited-item)]
                    (-> db (assoc-in current-path (reader/string->mixed edited-item))
                           (update-in [:developer :re-frame-browser/meta-items :edit-item?] not)))
               (let [original-item (get-in db current-path)
                     ; A string típusú értékeket a pretty/mixed->string függvény időzéjelek közé tenné ...
                     unparsed-item (if (string? original-item) original-item (pretty/mixed->string original-item))]
                    (-> db (assoc-in  [:developer :re-frame-browser/meta-items :edited-item] unparsed-item)
                           (update-in [:developer :re-frame-browser/meta-items :edit-item?]  not))))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :re-frame-browser/go-to! go-to!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :re-frame-browser/go-up! go-up!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :re-frame-browser/toggle-subscription! toggle-subscription!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :re-frame-browser/toggle-data-view! toggle-data-view!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :re-frame-browser/toggle-edit-item-mode! toggle-edit-item-mode!)
