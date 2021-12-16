
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.8.2
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.engine
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.eql       :as eql]
              [mid-fruits.keyword   :as keyword]
              [mid-fruits.map       :refer [dissoc-in]]
              [mid-fruits.vector    :as vector]
              [mid-fruits.time      :as time]
              [x.app-activities.api :as activities]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]
              [x.app-elements.api   :as elements]
              [x.app-router.api     :as router]
              [x.app-sync.api       :as sync]
              [mid-plugins.item-editor.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-editor.engine
(def editor-uri              engine/editor-uri)
(def form-id                 engine/form-id)
(def item-id->new-item?      engine/item-id->new-item?)
(def item-id->form-label     engine/item-id->form-label)
(def item-id-key             engine/item-id-key)
(def request-id              engine/request-id)
(def mutation-name           engine/mutation-name)
(def resolver-id             engine/resolver-id)
(def route-id                engine/route-id)
(def extended-route-id       engine/extended-route-id)
(def route-template          engine/route-template)
(def extended-route-template engine/extended-route-template)
(def parent-uri              engine/parent-uri)
(def render-event            engine/render-event)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-derived-item-id
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (string)
  [db [_ extension-id item-namespace]]
  (let [item-id-key (item-id-key extension-id item-namespace)]
       (r router/get-current-route-path-param db item-id-key)))

(defn synchronizing?
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [request-id (request-id extension-id item-namespace)]
       (r sync/listening-to-request? db request-id)))

(defn new-item?
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [item-id (get-in db [extension-id :editor-meta :item-id])]
       (item-id->new-item?  extension-id item-namespace item-id)))

(defn- get-backup-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) item-id
  [db [_ extension-id item-namespace item-id]]
  (get-in db [extension-id :editor-meta :backup-items item-id]))

(defn get-description
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (string)
  [db [_ extension-id item-namespace]]
  (if (r new-item? db extension-id item-namespace)
      (return "")
      (let [modified-at        (get-in db [extension-id :editor-data :modified-at])
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
  ;   :synchronizing? (boolean)
  ;   :new-item? (boolean)}
  [db [_ extension-id item-namespace]]
  {:colors         (get-in db [extension-id :editor-data :colors])
   :new-item?      (r new-item?      db extension-id item-namespace)
   :synchronizing? (r synchronizing? db extension-id item-namespace)})

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
  ;   :favorite? (boolean)
  ;   :form-completed? (boolean)
  ;   :new-item? (boolean)
  ;   :synchronizing? (boolean)}
  [db [_ extension-id item-namespace]]
  (let [form-id (form-id extension-id item-namespace)]
       {:archived?       (get-in db [extension-id :editor-data :archived?])
        :favorite?       (get-in db [extension-id :editor-data :favorite?])
        :form-completed? (r elements/form-completed? db form-id)
        :new-item?       (r new-item?                db extension-id item-namespace)
        :synchronizing?  (r synchronizing?           db extension-id item-namespace)}))

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
  ;   :new-item? (boolean)
  ;   :synchronizing? (boolean)}
  [db [_ extension-id item-namespace]]
  {:description    (r get-description db extension-id item-namespace)
   :new-item?      (r new-item?       db extension-id item-namespace)
   :synchronizing? (r synchronizing?  db extension-id item-namespace)})

; @usage
;  [:item-editor/get-view-props :my-namespace :my-type]
(a/reg-sub :item-editor/get-view-props get-view-props)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-item-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (-> db (dissoc-in [extension-id :editor-data])
         (dissoc-in [extension-id :editor-meta])))

(defn mark-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) mark-props
  ;  {:marker-key (keyword)}
  ;
  ; @return (map)
  [db [_ extension-id _ {:keys [marker-key]}]]
  (assoc-in db [extension-id :editor-data marker-key] true))

(defn unmark-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) mark-props
  ;  {:marker-key (keyword)}
  ;
  ; @return (map)
  [db [_ extension-id _ {:keys [marker-key]}]]
  (dissoc-in db [extension-id :editor-data marker-key]))

(defn- store-item-backup!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  [db [_ extension-id item-namespace item-id]]
  ; Az egyes elemek kliens oldalon tárolt változatáról készített backup másolatok az elem
  ; azonosítójával vannak tárolva. Így egy időben több elemről is lehetséges backup másolatot
  ; tárolni.
  ; A gyors egymás utánban kitörölt elemek törlésének visszavonhatósága átfedésbe kerülhet egymással,
  ; amiért szükséges az egyes elemekről készült backup másolatokat azonosítóval megkülönböztetve
  ; kezelni és tárolni.
  (let [backup-item (get-in db [extension-id :editor-data])]
       (assoc-in db [extension-id :editor-meta :backup-items item-id] backup-item)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/mark-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) mark-props
  ;  {:marker-key (keyword)
  ;   :marked-message (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [:item-editor/mark-item! :my-extension :my-type {:marker-key     :favorite?
  ;                                                   :marked-message :added-to-favorites}]
  (fn [{:keys [db]} [_ extension-id item-namespace {:keys [marker-key marked-message] :as mark-props}]]
      (let [mutation-name (mutation-name extension-id item-namespace :merge)
            item-id       (get-in db [extension-id :editor-meta :item-id])
            item-props    {marker-key true :id item-id}
            document      (db/document->namespaced-document item-props item-namespace)]
           {:db       (r mark-item! db extension-id item-namespace mark-props)
            :dispatch [:sync/send-query! (request-id extension-id item-namespace)
                                         {:on-success [:ui/blow-bubble! {:body {:content marked-message}}]
                                          :on-failure [:item-editor/->mark-item-failure extension-id item-namespace mark-props]
                                          :query      [`(~(symbol mutation-name) ~document)]}]})))

(a/reg-event-fx
  :item-editor/unmark-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) mark-props
  ;  {:marker-key (keyword)
  ;   :unmarked-message (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [:item-editor/unmark-item! :my-extension :my-type {:marker-key       :favorite?
  ;                                                     :unmarked-message :removed-from-favorites}]
  (fn [{:keys [db]} [_ extension-id item-namespace {:keys [marker-key unmarked-message] :as mark-props}]]
      (let [mutation-name (mutation-name extension-id item-namespace :merge)
            item-id       (get-in db [extension-id :editor-meta :item-id])
            item-props    {marker-key false :id item-id}
            document      (db/document->namespaced-document item-props item-namespace)]
           {:db       (r unmark-item! db extension-id item-namespace mark-props)
            :dispatch [:sync/send-query! (request-id extension-id item-namespace)
                                         {:on-success [:ui/blow-bubble! {:body {:content unmarked-message}}]
                                          :on-failure [:item-editor/->unmark-item-failure extension-id item-namespace mark-props]
                                          :query      [`(~(symbol mutation-name) ~document)]}]})))

(a/reg-event-fx
  :item-editor/add-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [parent-uri    (parent-uri    extension-id item-namespace)
            mutation-name (mutation-name extension-id item-namespace :add)
            item-props    (get-in    db [extension-id :editor-data])
            document      (db/document->namespaced-document item-props item-namespace)]
           [:sync/send-query! (request-id extension-id item-namespace)
                              {:on-stalled [:router/go-to! parent-uri]
                               :on-failure [:ui/blow-bubble! {:content {:body :failed-to-save}}]
                               :query      [`(~(symbol mutation-name) ~document)]}])))

(a/reg-event-fx
  :item-editor/save-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [parent-uri    (parent-uri    extension-id item-namespace)
            mutation-name (mutation-name extension-id item-namespace :save)
            item-props    (get-in    db [extension-id :editor-data])
            document      (db/document->namespaced-document item-props item-namespace)]
           [:sync/send-query! (request-id extension-id item-namespace)
                              {:on-stalled [:router/go-to! parent-uri]
                               :on-failure [:ui/blow-bubble! {:content {:body :failed-to-save}}]
                               :query      [`(~(symbol mutation-name) ~document)]}])))

(a/reg-event-fx
  :item-editor/delete-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [mutation-name (mutation-name extension-id item-namespace :delete)
            item-id-key   (item-id-key   extension-id item-namespace)
            item-id       (get-in    db [extension-id :editor-meta :item-id])]
           {:db (r store-item-backup! db extension-id item-namespace item-id)
            :dispatch [:sync/send-query! (request-id extension-id item-namespace)
                                         {:on-stalled [:item-editor/->item-deleted extension-id item-namespace item-id]
                                          :on-failure [:ui/blow-bubble! {:content {:body :failed-to-delete}}]
                                          :query      [`(~(symbol mutation-name) ~{item-id-key item-id})]}]})))

(a/reg-event-fx
  :item-editor/undo-delete!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]
      (let [backup-item   (r get-backup-item db extension-id item-namespace item-id)
            mutation-name (mutation-name extension-id item-namespace :undo-delete)
            item-id-key   (item-id-key   extension-id item-namespace)]
           [:sync/send-query! (request-id extension-id item-namespace)
                              {:on-stalled [:item-editor/->delete-undid extension-id item-namespace item-id]
                               :on-failure [:ui/blow-bubble! {:content {:body :failed-to-undo-delete}}]
                               :query      [`(~(symbol mutation-name) ~backup-item)]}])))

(a/reg-event-fx
  :item-editor/duplicate-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [mutation-name (mutation-name extension-id item-namespace :duplicate)
            item-id-key   (item-id-key   extension-id item-namespace)
            item-id       (get-in    db [extension-id :editor-meta :item-id])]
           [:sync/send-query! (request-id extension-id item-namespace)
                              {:on-stalled [:item-editor/->item-duplicated extension-id item-namespace]
                               :on-failure [:ui/blow-bubble! {:content {:body :failed-to-copy}}]
                               :query      [`(~(symbol mutation-name) ~{item-id-key item-id})]}])))

(a/reg-event-fx
  :item-editor/edit-copy!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn []))

(a/reg-event-fx
  :item-editor/receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @usage
  ;  [:item-editor/receive-item! :my-extension :my-type {...}]
  (fn [{:keys [db]} [_ extension-id item-namespace server-response]]
      (let [item-id     (get-in db [extension-id :editor-meta :item-id])
            item-entity (eql/id->entity item-id item-namespace)
            document    (get server-response item-entity)
            resolver-id (resolver-id extension-id item-namespace :suggestions)
            suggestions (get server-response resolver-id)
            ; XXX#3907
            ; Az item-lister modullal megegyezően az item-editor is névtér nélkül tárolja
            ; a letöltött dokumentumot.
            document (db/document->non-namespaced-document document)]
           {:db (-> db (assoc-in [extension-id :editor-data] document)
                       (assoc-in [extension-id :editor-meta :suggestions] suggestions))})))

(a/reg-event-fx
  :item-editor/request-new-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) editor-props
  ;  {:suggestion-keys (keywords in vector)(opt)}
  (fn [{:keys [db]} [_ extension-id item-namespace {:keys [suggestion-keys]}]]
      (when (vector/nonempty? suggestion-keys)
            [:sync/send-query! (request-id extension-id item-namespace)
                               {:on-stalled [:item-editor/receive-item! extension-id item-namespace]
                                        ; Get-suggestions resolver
                                :query [(let [resolver-id (resolver-id extension-id item-namespace :suggestions)]
                                            `(~resolver-id {:suggestion-keys ~suggestion-keys}))]}])))

(a/reg-event-fx
  :item-editor/request-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) editor-props
  ;  {:suggestion-keys (keywords in vector)(opt)}
  (fn [{:keys [db]} [_ extension-id item-namespace {:keys [suggestion-keys]}]]
      (let [item-id (get-in db [extension-id :editor-meta :item-id])]
           [:sync/send-query! (request-id extension-id item-namespace)
                              {:on-stalled [:item-editor/receive-item! extension-id item-namespace]
                                       ; Get-item resolver
                               :query [(let [entity       (eql/id->entity item-id item-namespace)
                                             added-at-key (keyword item-namespace "added-at")]
                                            {entity [added-at-key '*]})
                                       ; Get-suggestions resolver
                                       (when (vector/nonempty? suggestion-keys)
                                             (let [resolver-id (resolver-id extension-id item-namespace :suggestions)]
                                                 `(~resolver-id {:suggestion-keys ~suggestion-keys})))]}])))

(a/reg-event-fx
  :item-editor/load!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) editor-props
  ;  {:suggestion-keys (keywords in vector)(opt)}
  (fn [{:keys [db]} [_ extension-id item-namespace editor-props]]
      (let [derived-item-id (r get-derived-item-id db extension-id item-namespace)
            header-label    (item-id->form-label      extension-id item-namespace derived-item-id)
            new-item?       (item-id->new-item? extension-id item-namespace derived-item-id)]
           {:db (as-> db % (r reset-item-editor! % extension-id)
                           (assoc-in % [extension-id :editor-meta :item-id] derived-item-id))
            :dispatch-n [[:ui/listen-to-process! (request-id extension-id item-namespace)]
                         [:ui/set-header-title!  (param      header-label)]
                         [:ui/set-window-title!  (param      header-label)]
                         (if new-item? [:item-editor/request-new-item! extension-id item-namespace editor-props]
                                       [:item-editor/request-item!     extension-id item-namespace editor-props])
                         (render-event extension-id item-namespace)]})))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/->mark-item-failure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) mark-props
  ;  {:marker-key (keyword)}
  ;
  ; @usage
  ;  [:item-editor/->mark-item-failure :my-extension :my-type {:marker-key :favorite?}]
  (fn [{:keys [db]} [_ extension-id item-namespace {:keys [marker-key] :as mark-props}]]
      {:db       (r unmark-item! db extension-id item-namespace mark-props)
       :dispatch [:ui/blow-bubble! {:body {:content :network-error}}]}))

(a/reg-event-fx
  :item-editor/->unmark-item-failure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) mark-props
  ;  {:marker-key (keyword)}
  ;
  ; @usage
  ;  [:item-editor/->unmark-item-failure :my-extension :my-type {:marker-key :favorite?}]
  (fn [{:keys [db]} [_ extension-id item-namespace {:keys [marker-key] :as mark-props}]]
      {:db       (r mark-item! db extension-id item-namespace mark-props)
       :dispatch [:ui/blow-bubble! {:body {:content :network-error}}]}))

(a/reg-event-fx
  :item-editor/->item-duplicated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @usage
  ;  [:item-editor/->item-duplicated :my-extension :my-type {...}]
  (fn [{:keys [db]} [_ extension-id item-namespace server-response]]
      (let [item-id-key   (keyword item-namespace "id")
            mutation-name (mutation-name extension-id item-namespace :duplicate)
            mutation-name (symbol mutation-name)
            item-id       (get-in server-response [mutation-name item-id-key])
            editor-uri    (editor-uri extension-id item-namespace item-id)]
           [:ui/blow-bubble! {}])))

(a/reg-event-fx
  :item-editor/->item-deleted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]
      (let [parent-uri (parent-uri extension-id item-namespace)]
           {:dispatch-n [[:router/go-to! parent-uri]
                         [:item-editor/render-undo-delete-dialog! extension-id item-namespace item-id]]})))

(a/reg-event-fx
  :item-editor/->delete-undid
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]
      (let [editor-uri (editor-uri extension-id item-namespace item-id)]
           [:router/go-to! editor-uri])))
