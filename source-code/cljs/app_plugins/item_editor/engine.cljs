
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.4.0
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.engine
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.eql       :as eql]
              [mid-fruits.keyword   :as keyword]
              [mid-fruits.map       :refer [dissoc-in]]
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
(def item-id->new-item?      engine/item-id->new-item?)
(def item-id->form-label     engine/item-id->form-label)
(def item-id->item-uri       engine/item-id->item-uri)
(def item-id-key             engine/item-id-key)
(def request-id              engine/request-id)
(def mutation-name           engine/mutation-name)
(def form-id                 engine/form-id)
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
  ; @usage
  ;  (r item-editor/get-derived-item-id db :my-namespace :my-type)
  ;
  ; @return (keyword)
  [db [_ extension-id item-namespace]]
  (let [item-id-key (item-id-key extension-id item-namespace)]
       (r router/get-current-route-path-param db item-id-key)))

; @usage
;  [:item-editor/get-derived-item-id :my-namespace :my-type]
(a/reg-sub :item-editor/get-derived-item-id get-derived-item-id)

(defn synchronizing?
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (r item-editor/synchronizing? db :my-namespace :my-type)
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [request-id (request-id extension-id item-namespace)]
       (r sync/listening-to-request? db request-id)))

; @usage
;  [:item-editor/synchronizing? :my-namespace :my-type]
(a/reg-sub :item-editor/synchronizing? synchronizing?)

(defn new-item?
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (r item-editor/new-item? db :my-extension :my-type)
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [item-id (get-in db [extension-id :editor-meta :item-id])]
       (item-id->new-item?  extension-id item-namespace item-id)))

; @usage
;  [:item-editor/new-item? :my-namespace :my-type]
(a/reg-sub :item-editor/new-item? new-item?)

(defn get-description
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (r item-editor/get-description db :my-extension :my-type)
  ;
  ; @return (string)
  [db [_ extension-id item-namespace]]
  (if (r new-item? db extension-id item-namespace)
      (return "")
      (let [modified-at-key (keyword item-namespace "modified-at")
            modified-at     (get-in db [extension-id :editor-data modified-at-key])
            modified-at     (r activities/get-actual-timestamp db modified-at)]
           (components/content {:content :last-modified-at-n :replacements [modified-at]}))))

; @usage
;  [:item-editor/get-description :my-namespace :my-type]
(a/reg-sub :item-editor/get-description get-description)

(defn get-body-props
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (r item-editor/get-body-props db :my-extension :my-type)
  ;
  ; @return (map)
  ;  {:new-item? (boolean)}
  [db [_ extension-id item-namespace]]
  {:new-item? (r new-item? db extension-id item-namespace)})

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
  ;  {:form-completed? (boolean)
  ;   :new-item? (boolean)}
  [db [_ extension-id item-namespace]]
  (let [form-id (form-id extension-id item-namespace)]
       {:form-completed? (r elements/form-completed? db form-id)
        :new-item?       (r new-item?                db extension-id item-namespace)}))

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



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/add-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-editor/add-item! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [parent-uri    (parent-uri    extension-id item-namespace)
            mutation-name (mutation-name extension-id item-namespace :add)
            item-props    (get-in    db [extension-id :editor-data])]
           [:sync/send-query! (request-id extension-id item-namespace)
                              {:on-stalled [:router/go-to! parent-uri]
                               :on-failure [:ui/blow-bubble! {:content :saving-error :color :warning}]
                               :query      [`(~(symbol mutation-name) ~item-props)]}])))

(a/reg-event-fx
  :item-editor/update-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-editor/update-item! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [parent-uri    (parent-uri    extension-id item-namespace)
            mutation-name (mutation-name extension-id item-namespace :update)
            item-props    (get-in    db [extension-id :editor-data])]
           [:sync/send-query! (request-id extension-id item-namespace)
                              {:on-stalled [:router/go-to! parent-uri]
                               :on-failure [:ui/blow-bubble! {:content :saving-error :color :warning}]
                               :query      [`(~(symbol mutation-name) ~item-props)]}])))

(a/reg-event-fx
  :item-editor/delete-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-editor/delete-item! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [parent-uri    (parent-uri    extension-id item-namespace)
            mutation-name (mutation-name extension-id item-namespace "delete")
            item-id-key   (item-id-key   extension-id item-namespace)
            item-id       (get-in    db [extension-id :editor-meta :item-id])]
           [:sync/send-query! (request-id extension-id item-namespace)
                              {:on-stalled [:router/go-to! parent-uri]
                               :on-failure [:ui/blow-bubble! {:content :deleting-error :color :warning}]
                               :query      [`(~(symbol mutation-name) ~{item-id-key item-id})]}])))

(a/reg-event-fx
  :item-editor/duplicate-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-editor/duplicate-item! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [mutation-name (mutation-name extension-id item-namespace "duplicate")
            item-id-key   (item-id-key   extension-id item-namespace)
            item-id       (get-in    db [extension-id :editor-meta :item-id])]
           [:sync/send-query! (request-id extension-id item-namespace)
                              {:on-stalled [:item-editor/->item-duplicated extension-id item-namespace]
                               :on-failure [:ui/blow-bubble! {:content :copying-error :color :warning}]
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
            document    (get server-response item-entity)]
           {:db (assoc-in db [extension-id :editor-data] document)})))

(a/reg-event-fx
  :item-editor/request-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-editor/request-item! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [item-id      (get-in db [extension-id :editor-meta :item-id])
            entity       (eql/id->entity item-id item-namespace)
            added-at-key (keyword item-namespace "added-at")]
           [:sync/send-query! (request-id extension-id item-namespace)
                              {:on-stalled [:item-editor/receive-item! extension-id item-namespace]
                               :query      [{entity [added-at-key '*]}]}])))

(a/reg-event-fx
  :item-editor/load!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-editor/load! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [derived-item-id (r get-derived-item-id db extension-id item-namespace)
            new-item?       (item-id->new-item?       extension-id item-namespace derived-item-id)
            header-label    (item-id->form-label      extension-id item-namespace derived-item-id)]
           {:db         (-> db (dissoc-in [extension-id :editor-data])
                               (dissoc-in [extension-id :editor-meta])
                               (assoc-in  [extension-id :editor-meta :item-id] derived-item-id))
            :dispatch-n [[:ui/listen-to-process! (request-id extension-id item-namespace)]
                         [:ui/set-header-title!  (param      header-label)]
                         [:ui/set-window-title!  (param      header-label)]
                         (if-not new-item? [:item-editor/request-item! extension-id item-namespace])
                         (render-event extension-id item-namespace)]})))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

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
            edit-copy-uri (item-id->item-uri extension-id item-namespace item-id)]
           [:ui/blow-bubble! {}])))
