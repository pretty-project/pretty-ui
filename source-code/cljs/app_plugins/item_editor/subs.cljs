
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.18
; Description:
; Version: v1.2.6
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.subs
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.vector    :as vector]
              [x.app-activities.api :as activities]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]
              [x.app-elements.api   :as elements]
              [x.app-router.api     :as router]
              [x.app-sync.api       :as sync]
              [app-plugins.item-editor.engine :as engine]
              [mid-plugins.item-editor.subs   :as subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-editor.subs
(def get-editor-props subs/get-editor-props)
(def get-meta-item    subs/get-meta-item)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-current-item-id
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (r item-editor/get-current-item-id db :my-extension :my-type)
  ;
  ; @return (string)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :item-id))

(defn get-data-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (r subs/get-data-item :my-extension :my-type)
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (get-in db [extension-id :item-editor/data-items]))

(defn get-data-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) item-key
  ;
  ; @usage
  ;  (r subs/get-data-value :my-extension :my-type :modified-at)
  ;
  ; @return (map)
  [db [_ extension-id _ item-key]]
  (get-in db [extension-id :item-editor/data-items item-key]))

(defn get-derived-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (string)
  [db [_ extension-id _]]
  (r router/get-current-route-path-param db :item-id))

(defn editing-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (string)
  [db [_ extension-id item-namespace item-id]]
  ; - Az editing-item? függvény az item-id azonosítót a derived-item-id azonosítóval összehasonlítva
  ;   állapítja meg, hogy az item-id azonosítójú elem szerkesztés alatt áll-e.
  ; - A get-current-item-id függvény visszatérési értéke, csak abban az esetben felhasználható,
  ;   amikor az item-editor plugin van betöltve, annak kilépése után az adatbázisban maradt item-id
  ;   érték nem felhasználható!
  (let [derived-item-id (r get-derived-item-id db extension-id item-namespace)]
       (= item-id derived-item-id)))

(defn get-editor-title
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (metamorphic-content)
  [db [_ extension-id item-namespace]]
  (let [derived-item-id (r get-derived-item-id db extension-id item-namespace)]
       (engine/item-id->editor-title extension-id item-namespace derived-item-id)))

(defn get-current-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (get-in db [extension-id :item-editor/data-items]))

(defn export-current-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (namespaced map)
  [db [_ extension-id item-namespace]]
  (let [current-item (r get-current-item db extension-id item-namespace)]
       (db/document->namespaced-document current-item item-namespace)))

(defn export-copy-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (namespaced map)
  [db [_ extension-id item-namespace]]
  (let [current-item (r get-current-item db extension-id item-namespace)]
       (db/document->namespaced-document current-item item-namespace)))

(defn synchronizing?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [request-id (engine/request-id extension-id item-namespace)]
       (r sync/listening-to-request? db request-id)))

(defn error-mode?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :error-mode?))

(defn new-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [current-item-id (r get-current-item-id db extension-id item-namespace)]
       (engine/item-id->new-item? extension-id item-namespace current-item-id)))

(defn download-suggestions?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [suggestion-keys (r get-meta-item db extension-id item-namespace :suggestion-keys)]
       (vector/nonempty? suggestion-keys)))

(defn download-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [new-item?      (r new-item?     db extension-id item-namespace)
        recovery-mode? (r get-meta-item db extension-id item-namespace :recovery-mode?)]
       (not (or new-item? recovery-mode?))))

(defn download-data?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (or (r download-suggestions? db extension-id item-namespace)
      (r download-item?        db extension-id item-namespace)))

(defn editor-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (boolean (if-let [download-data? (r download-data? db extension-id item-namespace)]
                   (let [data-received? (r get-meta-item  db extension-id item-namespace :data-received?)
                         synchronizing? (r synchronizing? db extension-id item-namespace)]
                        ; XXX#3219
                        ; Azért szükséges vizsgálni az {:data-received? ...} tulajdonság értékét, hogy
                        ; a szerkesztő {:disabled? true} állapotban legyen, amíg NEM kezdődött még el
                        ; az adatok letöltése!
                        (or synchronizing? (not data-received?))))))

(defn get-backup-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  (get-in db [extension-id :item-editor/backup-items item-id]))

(defn export-backup-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (namespaced map)
  [db [_ extension-id item-namespace item-id]]
  (let [backup-item (r get-backup-item db extension-id item-namespace item-id)]
       (db/document->namespaced-document backup-item item-namespace)))

(defn get-local-changes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [current-item-id (r get-current-item-id db extension-id item-namespace)]
       (get-in db [extension-id :item-editor/local-changes current-item-id])))

(defn get-recovered-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [current-item-id (r get-current-item-id db extension-id item-namespace)
        backup-item     (r get-backup-item     db extension-id item-namespace current-item-id)
        local-changes   (r get-local-changes   db extension-id item-namespace)]
       (merge backup-item local-changes)))


(defn item-changed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [current-item-id (r get-current-item-id db extension-id item-namespace)
        current-item    (r get-current-item    db extension-id item-namespace)
        backup-item     (r get-backup-item     db extension-id item-namespace current-item-id)]
       (not= current-item backup-item)))

(defn route-handled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [route-id (r router/get-current-route-id db)]
       (= route-id (engine/route-id extension-id item-namespace))))

(defn set-title?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (r route-handled? db extension-id item-namespace))

(defn get-description
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (string)
  [db [_ extension-id item-namespace]]
  (if-not (r new-item? db extension-id item-namespace)
          (let [modified-at        (r get-data-value db extension-id item-namespace :modified-at)
                actual-modified-at (r activities/get-actual-timestamp db modified-at)]
               (components/content {:content :last-modified-at-n :replacements [actual-modified-at]}))))

(defn form-completed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [form-id (engine/form-id extension-id item-namespace)]
       (r elements/form-completed? db form-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-editor/get-data-item :my-extension :my-type]
(a/reg-sub :item-editor/get-data-item get-data-item)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-editor/get-data-value :my-extension :my-type]
(a/reg-sub :item-editor/get-data-value get-data-value)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-editor/editor-disabled? :my-extension :my-type]
(a/reg-sub :item-editor/editor-disabled? editor-disabled?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-editor/error-mode? :my-extension :my-type]
(a/reg-sub :item-editor/error-mode? error-mode?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-editor/form-completed? :my-extension :my-type]
(a/reg-sub :item-editor/form-completed? form-completed?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-editor/get-description :my-extension :my-type]
(a/reg-sub :item-editor/get-description get-description)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-editor/new-item? :my-extension :my-type]
(a/reg-sub :item-editor/new-item? new-item?)
