
(ns plugins.item-editor.engine
    (:require [mid-fruits.eql       :as eql]
              [mid-fruits.keyword   :as keyword]
              [mid-fruits.map       :refer [dissoc-in]]
              [mid-fruits.time      :as time]
              [x.app-activities.api :as activities]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]
              [x.app-elements.api   :as elements]
              [x.app-router.api     :as router]
              [x.app-sync.api       :as sync]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-id->new-item?
  ; @param (string) item-id
  ; @param (string) item-name
  ;
  ; @example
  ;  (item-editor/item-id->new-item? "new-product" "product")
  ;  =>
  ; true
  ;
  ; @example
  ;  (item-editor/item-id->new-item? "my-product" "product")
  ;  =>
  ; false
  ;
  ; @return (boolean)
  [item-id item-name]
  (= item-id (str "new-" item-name)))

(defn item-id->form-label
  ; @param (string) item-id
  ; @param (string) item-name
  ;
  ; @example
  ;  (item-editor/item-id->form-label "new-product" "product")
  ;  =>
  ; :add-product
  ;
  ; @example
  ;  (item-editor/item-id->form-label "my-product" "product")
  ;  =>
  ; :edit-product
  ;
  ; @return (metamorphic-content)
  [item-id item-name]
  (if (item-id->new-item? item-id item-name)
      (keyword/join "add-"  item-name)
      (keyword/join "edit-" item-name)))

(defn item-id->item-uri
  ; @param (string) extension-name
  ; @param (string) item-id
  ;
  ; @example
  ;  (item-editor/item-id->item-uri "products" "my-product")
  ;  =>
  ;  "/products/my-product"
  ;
  ; @return (string)
  [extension-name item-id]
  (str "/" extension-name "/" item-id))

(defn request-id
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @example
  ;  (item-editor/request-id "products" "product")
  ;  =>
  ;  :product-editor/synchronize!
  ;
  ; @return (keyword)
  [extension-name item-name]
  (keyword (str item-name "-editor") "synchronize!"))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn synchronizing?
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @return (boolean)
  [db [_ extension-name item-name]]
  (let [request-id (request-id extension-name item-name)]
       (r sync/listening-to-request? db request-id)))

(defn new-item?
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  (r item-editor/new-item? db "products" "product")
  ;
  ; @return (boolean)
  [db [_ extension-name item-name]]
  (let [extension-id (keyword extension-name)
        item-id      (get-in db [extension-id :editor-meta :item-id])]
       (item-id->new-item? item-id item-name)))

(defn get-description
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @return (string)
  [db [_ extension-name item-name]]
  (if (r new-item? db extension-name item-name)
      (str "")
      (let [extension-id    (keyword extension-name)
            namespace       (keyword item-name)
            modified-at-key (keyword namespace "modified-at")
            modified-at     (get-in db [extension-id :editor-data modified-at-key])
            modified-at     (r activities/get-actual-timestamp db modified-at)]
           (components/content {:content :last-modified-at-n :replacements [modified-at]}))))

(defn get-body-props
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @return (map)
  ;  {:new-item? (boolean)}
  [db [_ extension-name item-name]]
  {:new-item? (r new-item? db extension-name item-name)})

(defn get-header-props
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @return (map)
  ;  {:form-completed? (boolean)
  ;   :new-item? (boolean)}
  [db [_ extension-name item-name]]
  {:form-completed? (r elements/form-completed? db :item-editor)
   :new-item?       (r new-item?                db extension-name item-name)})

(defn get-view-props
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @return (map)
  ;  {:description (metamorphic-content)
  ;   :new-item? (boolean)
  ;   :synchronizing? (boolean)}
  [db [_ extension-name item-name]]
  {:description    (r get-description db extension-name item-name)
   :new-item?      (r new-item?       db extension-name item-name)
   :synchronizing? (r synchronizing?  db extension-name item-name)})



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/add-item!
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  [:item-editor/add-item! "products" "product"]
  (fn [{:keys [db]} [_ extension-name item-name]]
      (let [request-id    (request-id extension-name item-name)
            extension-id  (keyword extension-name)
            item-props    (get-in db [extension-id :editor-data])
            parent-path   (str "/" extension-name)
            mutation-name (str extension-name "/add-" item-name "-item!")]
          ;[:x.app-sync/send-query! :product-editor/synchronize!
           [:x.app-sync/send-query! request-id
                                    {:on-stalled [:x.app-router/go-to! parent-path]
                                     :on-failure [:x.app-ui/blow-bubble! {:content :saving-error :color :warning}]
                                     :query      [`(~(symbol mutation-name) ~item-props)]}])))

(a/reg-event-fx
  :item-editor/update-item!
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  [:item-editor/update-item! "products" "product"]
  (fn [{:keys [db]} [_ extension-name item-name]]
      (let [request-id    (request-id extension-name item-name)
            extension-id  (keyword extension-name)
            item-props    (get-in db [extension-id :editor-data])
            parent-path   (str "/" extension-name)
            mutation-name (str extension-name "/update-" item-name "-item!")]
          ;[:x.app-sync/send-query! :product-editor/synchronize!
           [:x.app-sync/send-query! request-id
                                    {:on-stalled [:x.app-router/go-to! parent-path]
                                     :on-failure [:x.app-ui/blow-bubble! {:content :saving-error :color :warning}]
                                     :query      [`(~(symbol mutation-name) ~item-props)]}])))

(a/reg-event-fx
  :item-editor/delete-item!
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  [:item-editor/delete-item! "products" "product"]
  (fn [{:keys [db]} [_ extension-name item-name]]
      (let [request-id    (request-id extension-name item-name)
            extension-id  (keyword extension-name)
            item-id-key   (keyword/join item-name "-id")
            item-id       (get-in db [extension-id :editor-meta :item-id])
            parent-path   (str "/" extension-name)
            mutation-name (str extension-name "/delete-" item-name "-item!")]
          ;[:x.app-sync/send-query! :product-editor/synchronize!
           [:x.app-sync/send-query! request-id
                                    {:on-stalled [:x.app-router/go-to! parent-path]
                                     :on-failure [:x.app-ui/blow-bubble! {:content :deleting-error :color :warning}]
                                     :query      [`(~(symbol mutation-name) ~{item-id-key item-id})]}])))

(a/reg-event-fx
  :item-editor/duplicate-item!
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  [:item-editor/duplicate-item! "products" "product"]
  (fn [{:keys [db]} [_ extension-name item-name]]
      (let [request-id    (request-id extension-name item-name)
            extension-id  (keyword extension-name)
            item-id-key   (keyword/join item-name "-id")
            item-id       (get-in db [extension-id :editor-meta :item-id])
            mutation-name (str extension-name "/duplicate-" item-name "-item!")]
          ;[:x.app-sync/send-query! :product-editor/synchronize!
           [:x.app-sync/send-query! request-id
                                    {:on-stalled [:item-editor/->item-duplicated extension-name item-name]
                                     :on-failure [:x.app-ui/blow-bubble! {:content :copying-error :color :warning}]
                                     :query      [`(~(symbol mutation-name) ~{item-id-key item-id})]}])))

(a/reg-event-fx
  :item-editor/edit-copy!
  (fn []))

(a/reg-event-fx
  :item-editor/->item-duplicated
  ; @param (string) extension-name
  ; @param (string) item-name
  ; @param (map) server-response
  ;
  ; @usage
  ;  [:item-editor/->item-duplicated "products" "product" {...}]
  (fn [{:keys [db]} [_ extension-name item-name server-response]]
      (let [extension-id  (keyword extension-name)
            namespace     (keyword extension-name)
            item-id-key   (keyword item-name "id")
            mutation-name (symbol (str extension-name "/duplicate-" item-name "-item!"))
            item-id       (get-in server-response [mutation-name item-id-key])
            edit-copy-uri (str "/" extension-name "/" item-id)]
           [:x.app-ui/blow-bubble! {}])))

(a/reg-event-fx
  :item-editor/receive-item!
  ; @param (string) extension-name
  ; @param (string) item-name
  ; @param (map) server-response
  ;
  ; @usage
  ;  [:item-editor/receive-item! "products" "product" {...}]
  (fn [{:keys [db]} [_ extension-name item-name server-response]]
      (let [extension-id (keyword extension-name)
            item-id      (get-in db [extension-id :editor-meta :item-id])
            namespace    (keyword item-name)
            entity       (eql/id->entity item-id namespace)
            document     (get server-response entity)]
           {:db (assoc-in db [extension-id :editor-data] document)})))

(a/reg-event-fx
  :item-editor/request-item!
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  [:item-editor/request-item! "products" "product"]
  (fn [{:keys [db]} [_ extension-name item-name]]
      (let [request-id   (request-id extension-name item-name)
            extension-id (keyword extension-name)
            item-id      (get-in db [extension-id :editor-meta :item-id])
            namespace    (keyword item-name)
            entity       (eql/id->entity item-id namespace)]
          ;[:x.app-sync/send-query! product-editor/synchronize!
           [:x.app-sync/send-query! request-id
                                    {:on-stalled [:item-editor/receive-item! extension-name item-name]
                                     :query      [{entity [:client/last-name '*]}]}])))

(a/reg-event-fx
  :item-editor/load!
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  [:item-editor/load! "products" "product"]
  (fn [{:keys [db]} [_ extension-name item-name]]
      (let [request-id   (request-id extension-name item-name)
            extension-id (keyword extension-name)
            item-id-key  (keyword/join item-name "-id")
            item-id      (r router/get-current-route-path-param db item-id-key)
            new-item?    (item-id->new-item?  item-id item-name)
            header-label (item-id->form-label item-id item-name)
            render-event (keyword extension-name (str "render-" item-name "-editor!"))]
                              ;(dissoc-in [:products :editor-data])
           {:db         (-> db (dissoc-in [extension-id :editor-data])
                              ;(dissoc-in [:products :editor-meta])
                               (dissoc-in [extension-id :editor-meta])
                              ;(assoc-in  [:products :editor-meta :product-id] "my-product")
                               (assoc-in  [extension-id :editor-meta :item-id] item-id))
                        ;[:x.app-ui/listen-to-process! :product-editor/synchronize!]
            :dispatch-n [[:x.app-ui/listen-to-process! request-id]
                        ;[:x.app-ui/set-header-title!  :edit-product]
                         [:x.app-ui/set-header-title!  header-label]
                        ;[:x.app-ui/set-header-title!  :edit-product]
                         [:x.app-ui/set-window-title!  header-label]
                                          ;[:products/request-product!]
                         (if-not new-item? [:item-editor/request-item! extension-name item-name])
                        ;[:product/render-product-editor!]
                         [render-event]]})))

(a/reg-event-fx
  :item-editor/add-route!
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  [:item-editor/add-route! "products" "product"]
  (fn [_ [_ extension-name item-name]]
           ;route-id :products/form-route
      (let [route-id (keyword extension-name "form-route")]
           [:x.app-router/add-route! route-id
                                     ;:route-template "/products/:product-id"
                                     {:route-template (str "/" extension-name "/:" item-name "-id")
                                     ;:route-parent   "/products"
                                      :route-parent   (str "/" extension-name)
                                     ;:route-event    [:item-editor/load! "products" "product"]
                                      :route-event    [:item-editor/load! extension-name item-name]
                                      :restricted?    true}])))
