
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

(defn extension-namespace
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-editor/extension-namespace :products :product)
  ;  =>
  ;  :product-editor
  ;
  ; @return (keyword)
  [_ item-namespace]
  (keyword/join item-namespace "-editor"))

(defn item-id->new-item?
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) item-id
  ;
  ; @example
  ;  (item-editor/item-id->new-item? :products :product :new-product)
  ;  =>
  ; true
  ;
  ; @example
  ;  (item-editor/item-id->new-item? :products :product :my-product)
  ;  =>
  ; false
  ;
  ; @return (boolean)
  [_ item-namespace item-id]
  (= item-id (keyword/join "new-" item-namespace)))

(defn item-id->form-label
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) item-id
  ;
  ; @example
  ;  (item-editor/item-id->form-label :products :product :new-product)
  ;  =>
  ; :add-product
  ;
  ; @example
  ;  (item-editor/item-id->form-label :products :product :my-product)
  ;  =>
  ; :edit-product
  ;
  ; @return (metamorphic-content)
  [extension-id item-namespace item-id]
  (if (new-item? extension-id item-namespace item-id)
      (keyword/join "add-"    item-namespace)
      (keyword/join "edit-"   item-namespace)))

(defn item-id->item-uri
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) item-id
  ;
  ; @example
  ;  (item-editor/item-id->item-uri :products :product :my-product)
  ;  =>
  ;  "/products/my-product"
  ;
  ; @return (string)
  [extension-id _ item-id]
  (str "/" (name extension-id) "/" (name item-id)))

(defn- parent-uri
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-editor/parent-uri :products :product)
  ;  =>
  ;  "/products"
  ;
  ; @return (string)
  [extension-id _]
  (str "/" (name extension-id)))

(defn request-id
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-editor/request-id :products :product)
  ;  =>
  ;  :product-editor/synchronize!
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (str (name item-namespace) "-editor") "synchronize!"))

(defn mutation-name
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) action-id
  ;
  ; @example
  ;  (item-editor/mutation-name :products :product :add)
  ;  =>
  ;  "products/add-product-item!"
  ;
  ; @return (string)
  [extension-id item-namespace action-id]
  (str (name extension-id) "/" (name action-id) "-" (name item-namespace) "-item!"))

(defn form-id
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-editor/form-id :products :product)
  ;  =>
  ;  :product-editor/form
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (str (name item-namespace) "-editor") "form"))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  ; @usage
  ;  (r item-editor/new-item? db :products :product)
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [item-id (get-in db [extension-id :editor-meta :item-id])]
       (item-id->new-item?  extension-id item-namespace item-id)))

(defn get-description
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (r item-editor/get-description db :products :product)
  ;
  ; @return (string)
  [db [_ extension-id item-namespace]]
  (if (r new-item? db extension-id item-namespace)
      (return "")
      (let [modified-at-key (keyword item-namespace "modified-at")
           ;modified-at     (get-in db [:products    :editor-data :product/modified-at])
            modified-at     (get-in db [extension-id :editor-data modified-at-key])
            modified-at     (r activities/get-actual-timestamp db modified-at)]
           (components/content {:content :last-modified-at-n :replacements [modified-at]}))))

(defn get-body-props
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  ;  {:new-item? (boolean)}
  [db [_ extension-id item-namespace]]
  {:new-item? (r new-item? db extension-id item-namespace)})

(defn get-header-props
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  ;  {:form-completed? (boolean)
  ;   :new-item? (boolean)}
  [db [_ extension-id item-namespace]]
  (let [form-id (form-id extension-id item-namespace)]
       {:form-completed? (r elements/form-completed? db form-id)
        :new-item?       (r new-item?                db extension-id item-namespace)}))

(defn get-view-props
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  ;  {:description (metamorphic-content)
  ;   :new-item? (boolean)
  ;   :synchronizing? (boolean)}
  [db [_ extension-id item-namespace]]
  {:description    (r get-description db extension-id item-namespace)
   :new-item?      (r new-item?       db extension-id item-namespace)
   :synchronizing? (r synchronizing?  db extension-id item-namespace)})



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
  ;  [:item-editor/add-item! :products :product]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [request-id    (request-id    extension-id item-namespace)
            parent-uri    (parent-uri    extension-id item-namespace)
            mutation-name (mutation-name extension-id item-namespace :add)
            item-props    (get-in    db [extension-id :editor-data])]
          ;[:x.app-sync/send-query! :product-editor/synchronize! {...}]
           [:x.app-sync/send-query! request-id
                                    ;:on-stalled [:x.app-router/go-to! "/products"]
                                    {:on-stalled [:x.app-router/go-to! parent-uri]
                                     :on-failure [:x.app-ui/blow-bubble! {:content :saving-error :color :warning}]
                                    ;:query      [`(~(symbol "products/add-product-item!") ~{...})]
                                     :query      [`(~(symbol mutation-name) ~item-props)]}])))

(a/reg-event-fx
  :item-editor/update-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-editor/update-item! :products :product]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [request-id    (request-id    extension-id item-namespace)
            parent-uri    (parent-uri    extension-id item-namespace)
            mutation-name (mutation-name extension-id item-namespace :update)
            item-props    (get-in    db [extension-id :editor-data])]
          ;[:x.app-sync/send-query! :product-editor/synchronize! {...}]
           [:x.app-sync/send-query! request-id
                                    ;:on-stalled [:x.app-router/go-to! "/products"]
                                    {:on-stalled [:x.app-router/go-to! parent-uri]
                                     :on-failure [:x.app-ui/blow-bubble! {:content :saving-error :color :warning}]
                                    ;:query      [`(~(symbol "products/update-product-item!") ~{...})]
                                     :query      [`(~(symbol mutation-name) ~item-props)]}])))

(a/reg-event-fx
  :item-editor/delete-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-editor/delete-item! :products :product]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [request-id    (request-id    extension-id item-namespace)
            parent-uri    (parent-uri    extension-id item-namespace)
            mutation-name (mutation-name extension-id item-namespace "delete")
            item-id       (get-in    db [extension-id :editor-meta :item-id])
            item-id-key   (keyword/join item-namespace "-id")]
          ;[:x.app-sync/send-query! :product-editor/synchronize! {...}]
           [:x.app-sync/send-query! request-id
                                    ;:on-stalled [:x.app-router/go-to! "/products"]
                                    {:on-stalled [:x.app-router/go-to! parent-uri]
                                     :on-failure [:x.app-ui/blow-bubble! {:content :deleting-error :color :warning}]
                                    ;:query      [`(~(symbol "products/delete-product-item!") ~{:product-id :my-product})]
                                     :query      [`(~(symbol mutation-name) ~{item-id-key item-id})]}])))

(a/reg-event-fx
  :item-editor/duplicate-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-editor/duplicate-item! :products :product]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [request-id    (request-id    extension-id item-namespace)
            mutation-name (mutation-name extension-id item-namespace "duplicate")
            item-id       (get-in    db [extension-id :editor-meta :item-id])
            item-id-key   (keyword/join item-namespace "-id")]
          ;[:x.app-sync/send-query! :product-editor/synchronize! {...}]
           [:x.app-sync/send-query! request-id
                                    ;:on-stalled [:item-editor/->item-duplicated :products :product]
                                    {:on-stalled [:item-editor/->item-duplicated extension-id item-namespace]
                                     :on-failure [:x.app-ui/blow-bubble! {:content :copying-error :color :warning}]
                                    ;:query      [`(~(symbol "products/duplicate-product-item!") ~{:product-id :my-product})]
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
  ;  [:item-editor/receive-item! :products :product {...}]
  (fn [{:keys [db]} [_ extension-id item-namespace server-response]]
      (let [item-id     (get-in db [extension-id :editor-meta :item-id])
            item-entity (eql/id->entity item-id item-namespace)
            document    (get server-response item-entity)]
           ;:db (assoc-in db [:products    :editor-data] {...})
           {:db (assoc-in db [extension-id :editor-data] document)})))

(a/reg-event-fx
  :item-editor/request-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-editor/request-item! :products :product]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [request-id (request-id extension-id item-namespace)
            item-id    (get-in db [extension-id :editor-meta :item-id])
            entity     (eql/id->entity item-id item-namespace)]
          ;[:x.app-sync/send-query! product-editor/synchronize! {...}]
           [:x.app-sync/send-query! request-id
                                    ;:on-stalled [:item-editor/receive-item! :products :product]
                                    {:on-stalled [:item-editor/receive-item! extension-id item-namespace]
                                    ;:query      [{[:product/id :my-product] [:client/last-name '*]}]
                                     :query      [{entity [:client/last-name '*]}]}])))

(a/reg-event-fx
  :item-editor/load!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-editor/load! :products :product]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [request-id   (request-id extension-id item-namespace)
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
                         (if-not new-item? [:item-editor/request-item! extension-id item-namespace])
                        ;[:product/render-product-editor!]
                         [render-event]]})))

(a/reg-event-fx
  :item-editor/initialize!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-editor/initialize! :products :product]
  (fn [_ [_ extension-id item-namespace]]
      (let [route-id (keyword extension-name "form-route")]
          ;[:x.app-router/add-route! :products/form-route {...}]
           [:x.app-router/add-route! route-id
                                     ;:route-template "/products/:product-id"
                                     {:route-template (str "/" extension-name "/:" item-name "-id")
                                     ;:route-parent   "/products"
                                      :route-parent   (str "/" extension-name)
                                     ;:route-event    [:item-editor/load! :products :product]
                                      :route-event    [:item-editor/load! extension-id item-namespace]
                                      :restricted?    true}])))
;



(a/reg-event-fx
  :item-editor/->item-duplicated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @usage
  ;  [:item-editor/->item-duplicated :products :product {...}]
  (fn [{:keys [db]} [_ extension-id item-namespace server-response]]
      (let [extension-id  (keyword extension-name)
            namespace     (keyword extension-name)
            item-id-key   (keyword item-name "id")
            mutation-name (symbol (str extension-name "/duplicate-" item-name "-item!"))
            item-id       (get-in server-response [mutation-name item-id-key])
            edit-copy-uri (str "/" extension-name "/" item-id)]
           [:x.app-ui/blow-bubble! {}])))
