
(ns extensions.pattern
    (:require [mid-fruits.keyword :as keyword]
              [mid-fruits.map     :refer [dissoc-in]]
              [x.app-core.api     :as a :refer [r]]
              [x.app-router.api   :as router]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-id->new-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) item-id
  ; @param (string) item-name
  ;
  ; @return (boolean)
  [item-id item-name]
  (= item-id (str "new-" item-name)))

(defn- item-id->header-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) item-id
  ; @param (string) item-name
  ;
  ; @return (metamorphic-content)
  [item-id item-name]
  (if (item-id->new-item? item-id item-name)
      (keyword/join "add-"  item-name)
      (keyword/join "edit-" item-name)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- new-item?
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  (r pattern/new-item? db "products" "product")
  ;
  ; @return (boolean)
  [db [_ extension-name item-name]]
  (let [extension-id (keyword extension-name)
        item-id-key  (keyword/join item-name "-id")
        item-id (get-in db [extension-id :form-meta item-id-key])]
       (item-id->new-item? item-id item-name)))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :extensions/load-item-list!
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  [:extensions/load-item-list! "products" "product"]
  (fn [{:keys [db]} [_ extension-name item-name]]
      (let [request-id   (keyword extension-name (str "synchronize-" item-name "-list!"))
            render-event (keyword extension-name (str "render-"      item-name "-list!"))
            extension-id (keyword extension-name)]
           {:db         (dissoc-in db [extension-id :list-data])
            :dispatch-n [[:x.app-ui/listen-to-process! request-id]
                         [:x.app-ui/set-header-title!  extension-id]
                         [:x.app-ui/set-window-title!  extension-id]
                         [render-event]]})))

(a/reg-event-fx
  :extensions/add-item-list-route!
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  [:extensions/add-item-list-route! "products" "product"]
  (fn [_ [_ extension-name item-name]]
      (let [route-id (keyword extension-name "list-route")]
           [:x.app-router/add-route! route-id
                                     {:route-template (str "/" extension-name)
                                      :route-event    [:extensions/load-item-list! extension-name item-name]
                                      :restricted?    true}])))

(a/reg-event-fx
  :extensions/load-item-form!
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  [:extensions/load-item-form! "products" "product"]
  (fn [{:keys [db]} [_ extension-name item-name]]
      (let [extension-id (keyword extension-name)
            item-id-key  (keyword/join item-name "-id")
            item-id      (r router/get-current-route-path-param db item-id-key)
            header-label (item-id->header-label item-id item-name)
            request-id   (keyword extension-name (str "synchronize-" item-name "-form!"))
            request-item-event (keyword extension-name (str "request-" item-name "!"))
            render-event       (keyword extension-name (str "render-"  item-name "-form!"))]
           {:db         (-> db (assoc-in  [extension-id :form-meta item-id-key] item-id)
                               (dissoc-in [extension-id :form-data]))
            :dispatch-n [[:x.app-ui/listen-to-process! request-id]
                         [:x.app-ui/set-header-title!  header-label]
                         [:x.app-ui/set-window-title!  header-label]
                         [request-item-event item-id]
                         [render-event]]})))

(a/reg-event-fx
  :extensions/add-item-form-route!
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  [:extensions/add-item-form-route! "products" "product"]
  (fn [_ [_ extension-name item-name]]
      (let [route-id (keyword extension-name "form-route")]
           [:x.app-router/add-route! route-id
                                     {:route-template (str "/" extension-name "/:" item-name "-id")
                                      :route-parent   (str "/" extension-name)
                                      :route-event    [:extensions/load-item-form! extension-name item-name]
                                      :restricted?    true}])))
