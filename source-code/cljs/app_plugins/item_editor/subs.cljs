
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.subs
    (:require [app-plugins.item-editor.engine :as engine]
              [mid-fruits.candy               :refer [param return]]
              [mid-fruits.keyword             :as keyword]
              [mid-fruits.uri                 :as uri]
              [mid-fruits.vector              :as vector]
              [x.app-activities.api           :as activities]
              [x.app-components.api           :as components]
              [x.app-core.api                 :as a :refer [r]]
              [x.app-db.api                   :as db]
              [x.app-elements.api             :as elements]
              [x.app-sync.api                 :as sync]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-editor-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (get-in db [extension-id :item-editor/meta-items]))

(defn get-inherited-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  ; Az item-editor plugin megőrzi a plugin szerver-oldalról érkezett beállításait.
  (let [editor-props (r get-editor-props db extension-id item-namespace)]
       (select-keys editor-props [:base-route :on-load :route-template :route-title]))) ; <- szerver-oldalról érkezett beállítások

(defn get-meta-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) item-key
  ;
  ; @return (*)
  [db [_ extension-id item-namespace item-key]]
  (get-in db [extension-id :item-editor/meta-items item-key]))

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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (r get-request-id db :my-extension :my-type)
  ;  =>
  ;  :my-handler/synchronize-editor!
  ;
  ; @return (keyword)
  [db [_ extension-id item-namespace]]
  ; XXX#3055
  (if-let [handler-key (r get-meta-item db extension-id item-namespace :handler-key)]
          (keyword (name handler-key) "synchronize-editor!")))

(defn get-mutation-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) action-key
  ;
  ; @example
  ;  (r get-resolver-id db :my-extension :my-type :delete)
  ;  =>
  ;  "my-handler/delete-item!"
  ;
  ; @return (string)
  [db [_ extension-id item-namespace action-key]]
  (let [handler-key (r get-meta-item db extension-id item-namespace :handler-key)]
       (str (name handler-key) "/"
            (name action-key)  "-item!")))

(defn get-resolver-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) action-key
  ;
  ; @example
  ;  (r get-resolver-id db :my-extension :my-type :get)
  ;  =>
  ;  :my-handler/get-item
  ;
  ; @return (keyword)
  [db [_ extension-id item-namespace action-key]]
  (let [handler-key (r get-meta-item db extension-id item-namespace :handler-key)]
       (keyword      (name handler-key)
                (str (name action-key) "-item"))))



;; ----------------------------------------------------------------------------
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

(defn get-item-route
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r item-editor/get-item-route db :my-extension :item-namespace "my-item")
  ;
  ; @return (string)
  [db [_ extension-id item-namespace item-id]]
  (if-let [base-route (r get-meta-item db extension-id item-namespace :base-route)]
          (str base-route "/" item-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn data-received?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [data-received? (r get-meta-item db extension-id item-namespace :data-received?)]
       (boolean data-received?)))

(defn editor-synchronizing?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  ; XXX#3055
  (if-let [request-id (r get-request-id db extension-id item-namespace)]
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
  (= (r get-current-item-id db extension-id item-namespace)
     (r get-meta-item       db extension-id item-namespace :new-item-id)))

(defn set-auto-title?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :auto-title?))

(defn get-auto-title_
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (metamorphic-content)
  [db [_ extension-id item-namespace]]
  (if-let [new-item? (r get-meta-item db extension-id item-namespace :new-item?)]
          (engine/add-item-label  extension-id item-namespace)
          (engine/edit-item-label extension-id item-namespace)))

(defn get-route-title
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (metamorphic-content)
  [db [_ extension-id item-namespace]]
  (let [route-title (r get-meta-item db extension-id item-namespace :route-title)]
       (case route-title :auto nil route-title)))

(defn get-auto-title
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (metamorphic-content)
  [db [_ extension-id item-namespace]])

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

(defn get-form-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (metamorphic-content)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :form-element))

(defn get-menu-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (metamorphic-content)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :menu-element))

(defn form-completed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (if-let [form-id (r get-meta-item db extension-id item-namespace :form-id)]
          (r elements/form-completed? db form-id)
          (return true)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
                   (let [data-received?        (r get-meta-item         db extension-id item-namespace :data-received?)
                         editor-synchronizing? (r editor-synchronizing? db extension-id item-namespace)]
                        ; XXX#3219
                        ; Azért szükséges vizsgálni az {:data-received? ...} tulajdonság értékét, hogy
                        ; a szerkesztő {:disabled? true} állapotban legyen, amíg NEM kezdődött még el
                        ; az adatok letöltése!
                        (or editor-synchronizing? (not data-received?))))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-copy-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @example
  ;  (r subs//server-response->copy-id :my-extension :my-type {my-handler/duplicate-item! {:my-type/id "my-item"}})
  ;  =>
  ;  "my-item"
  ;
  ; @return (string)
  [db [_ extension-id item-namespace server-response]]
  (let [item-id-key   (keyword/add-namespace item-namespace :id)
        mutation-name (r get-mutation-name db extension-id item-namespace :duplicate)]
       (get-in server-response [(symbol mutation-name) item-id-key])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) extension-id
; @param (keyword) item-namespace
; @param (keyword) item-key
;
; @usage
;  [:item-editor/get-meta-item :my-extension :my-type :my-item]
(a/reg-sub :item-editor/get-meta-item get-meta-item)

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
;  [:item-editor/data-received? :my-extension :my-type]
(a/reg-sub :item-editor/data-received? data-received?)

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
;  [:item-editor/get-form-element :my-extension :my-type]
(a/reg-sub :item-editor/get-form-element get-form-element)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-editor/get-menu-element :my-extension :my-type]
(a/reg-sub :item-editor/get-menu-element get-menu-element)

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

; @param (keyword) extension-id
; @param (keyword) item-namespace
; @param (string) item-id
;
; @usage
;  [:item-editor/get-item-route :my-extension :my-type "my-item"]
(a/reg-sub :item-editor/get-item-route get-item-route)
