
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.18
; Description:
; Version: v0.8.2
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.subs
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.eql       :as eql]
              [mid-fruits.vector    :as vector]
              [x.app-activities.api :as activities]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]
              [x.app-elements.api   :as elements]
              [x.app-router.api     :as router]
              [x.app-sync.api       :as sync]
              [app-plugins.item-editor.engine :as engine]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-derived-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (string)
  [db [_ extension-id]]
  (r router/get-current-route-path-param db :item-id))

(defn get-current-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (string)
  [db [_ extension-id]]
  (get-in db [extension-id :item-editor/meta-items :item-id]))

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
  (let [derived-item-id (r get-derived-item-id db extension-id)]
       (= item-id derived-item-id)))

(defn get-editor-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) editor-props
  ;  {:label (metamorphic-content)(opt)}
  ;
  ; @return (metamorphic-content)
  [db [_ extension-id item-namespace editor-props]]
  (if-let [label (get editor-props :label)]
          (return label)
          (let [derived-item-id (r get-derived-item-id db extension-id)]
               (engine/item-id->editor-label extension-id item-namespace derived-item-id))))

(defn get-current-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (get-in db [extension-id :item-editor/data-item]))

(defn export-current-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (namespaced map)
  [db [_ extension-id item-namespace]]
  (let [current-item (r get-current-item db extension-id)]
       (db/document->namespaced-document current-item item-namespace)))

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
  (get-in db [extension-id :item-editor/data-item item-key]))

(defn get-meta-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) item-key
  ;
  ; @usage
  ;  (r subs/get-meta-value :my-extension :my-type :error-mode?)
  ;
  ; @return (map)
  [db [_ extension-id _ item-key]]
  (get-in db [extension-id :item-editor/meta-items item-key]))

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

(defn new-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [current-item-id (r get-current-item-id db extension-id)]
       (engine/item-id->new-item? extension-id item-namespace current-item-id)))

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

(defn export-marked-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) mark-props
  ;  {:marker-key (keyword)
  ;   :toggle-f (function)}
  ;
  ; @example
  ;  (r subs/export-marked-item db :my-extension :my-type {:marker-key :archived? :toggle-f not})
  ;  =>
  ;  {:my-type/id "my-item" :my-type/archived? true}
  ;
  ; @return (namespaced map)
  [db [_ extension-id item-namespace {:keys [marker-key toggle-f]}]]
  (let [current-item-id (r get-current-item-id db extension-id)
        marker-value    (get-in db [extension-id :item-editor/data-item marker-key])
        marked-item     {:id current-item-id marker-key (toggle-f marker-value)}]
       (db/document->namespaced-document marked-item item-namespace)))

(defn get-local-changes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (let [current-item-id (r get-current-item-id db extension-id)]
       (get-in db [extension-id :item-editor/meta-items :local-changes current-item-id])))

(defn get-recovered-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [current-item-id (r get-current-item-id db extension-id)
        backup-item     (r get-backup-item     db extension-id item-namespace current-item-id)
        local-changes   (r get-local-changes   db extension-id)]
       (merge backup-item local-changes)))

(defn item-changed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [current-item-id (r get-current-item-id db extension-id)
        current-item    (r get-current-item    db extension-id)
        backup-item     (r get-backup-item     db extension-id item-namespace current-item-id)]
       ; Az elem {:archived? ...} és {:favorite? ...} tulajdonságait érintő változtatások
       ; (UX-szempontból) nem számítanak az elemen végzett változtatásnak!
       (not= (dissoc current-item :archived? :favorite?)
             (dissoc backup-item  :archived? :favorite?))))

(defn get-current-item-entity
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace]]
  (let [current-item-id (r get-current-item-id db extension-id)]
       (eql/id->entity current-item-id item-namespace)))

(defn download-suggestions?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [suggestion-keys (r get-meta-value db extension-id item-namespace :suggestion-keys)]
       (vector/nonempty? suggestion-keys)))

(defn download-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [new-item?      (r new-item?      db extension-id item-namespace)
        recovery-mode? (r get-meta-value db extension-id item-namespace :recovery-mode?)]
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

(defn get-description
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (string)
  [db [_ extension-id item-namespace]]
  (if-not (r new-item? db extension-id item-namespace)
          (let [modified-at        (r get-data-value db extension-id item-namespace :modified-at)
                actual-modified-at (r activities/get-actual-timestamp db modified-at)]
               (components/content {:content :last-modified-at-n :replacements [actual-modified-at]}))))

(defn get-body-props
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (r item-editor/get-body-props db :my-extension :my-type)
  ;
  ; @return (map)
  ;  {:colors (strings in vector)
  ;   :error-mode? (boolean)
  ;   :synchronizing? (boolean)
  ;   :new-item? (boolean)}
  [db [_ extension-id item-namespace]]
  (if-let [error-mode? (r get-meta-value db extension-id item-namespace :error-mode?)]
          {:error-mode? true}
          {:colors         (r get-data-value db extension-id item-namespace :colors)
           :new-item?      (r new-item?      db extension-id item-namespace)
           :synchronizing? (r synchronizing? db extension-id item-namespace)}))

; @usage
;  [:item-editor/get-body-props :my-namespace :my-type]
(a/reg-sub :item-editor/get-body-props get-body-props)

(defn get-header-props
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (r item-editor/get-header-props db :my-extension :my-type)
  ;
  ; @return (map)
  ;  {:archived? (boolean)
  ;   :error-mode? (boolean)
  ;   :favorite? (boolean)
  ;   :form-completed? (boolean)
  ;   :handle-archived-items? (boolean)
  ;   :handle-favorite-items? (boolean)
  ;   :new-item? (boolean)
  ;   :synchronizing? (boolean)}
  [db [_ extension-id item-namespace]]
  (if-let [error-mode? (r get-meta-value db extension-id item-namespace :error-mode?)]
          {:error-mode? true}
          (let [form-id (engine/form-id extension-id item-namespace)]
               {:archived?              (r get-data-value db extension-id item-namespace :archived?)
                :favorite?              (r get-data-value db extension-id item-namespace :favorite?)
                :handle-archived-items? (r get-meta-value db extension-id item-namespace :handle-archived-items?)
                :handle-favorite-items? (r get-meta-value db extension-id item-namespace :handle-favorite-items?)
                :new-item?              (r new-item?      db extension-id item-namespace)
                :synchronizing?         (r synchronizing? db extension-id item-namespace)
                :form-completed?        (r elements/form-completed? db form-id)})))

; @usage
;  [:item-editor/get-header-props :my-namespace :my-type]
(a/reg-sub :item-editor/get-header-props get-header-props)

(defn get-view-props
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (r item-editor/get-view-props db :my-extension :my-type)
  ;
  ; @return (map)
  ;  {:description (metamorphic-content)
  ;   :error-mode? (boolean)
  ;   :new-item? (boolean)}
  [db [_ extension-id item-namespace]]
  (if-let [error-mode? (r get-meta-value db extension-id item-namespace :error-mode?)]
          {:error-mode? true}
          {:description (r get-description db extension-id item-namespace)
           :new-item?   (r new-item?       db extension-id item-namespace)}))

; @usage
;  [:item-editor/get-view-props :my-namespace :my-type]
(a/reg-sub :item-editor/get-view-props get-view-props)
