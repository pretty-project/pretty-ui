
(ns plugins.view-selector.engine
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :refer [dissoc-in]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-router.api  :as router]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-id
  ; @param (keyword) extension-id
  ;
  ; @example
  ;  (view-selector/request-id :settings)
  ;  =>
  ;  :settings/synchronize!
  ;
  ; @return (keyword)
  [extension-id]
  (keyword extension-id :synchronize!))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-view
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r view-selector/get-selected-view db :settings)
  ;
  ; @return (keyword)
  [db [_ extension-id]]
  (get-in db [extension-id :selector-meta :selected-view]))

(defn get-body-props
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r view-selector/get-body-props db :settings)
  ;
  ; @return (map)
  ;  {:selected-view (keyword)}
  [db [_ extension-id]]
  {:selected-view (r get-selected-view db extension-id)})

(defn get-header-props
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r view-selector/get-header-props db :settings)
  ;
  ; @return (map)
  ;  {:selected-view (keyword)}
  [db [_ extension-id]]
  {:selected-view (r get-selected-view db extension-id)})



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn change-view!
  ; @param (keyword) extension-id
  ; @param (keyword) view-id
  ;
  ; @usage
  ;  (r view-selector/change-view! db :settings :privacy)
  ;
  ; @return (map)
  [db [_ extension-id view-id]]
  (assoc-in db [extension-id :selector-meta :selected-view] view-id))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :view-selector/load!
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:default-view (keyword)(opt)}
  ;
  ; @usage
  ;  [:view-selector/load! :settings {:default-view :privacy}]
  (fn [{:keys [db]} [_ extension-id {:keys [default-view]}]]
      (let [request-id    (request-id extension-id)
            render-event  (keyword    extension-id :render!)
            selected-view (or (r router/get-current-route-path-param db :selected-view)
                              (param default-view))]
           {:db         (-> db (dissoc-in [extension-id :selector-meta])
                              ;(assoc-in  [:settings :selector-meta :selected-view] :privacy)
                               (assoc-in  [extension-id :selector-meta :selected-view] selected-view))
                        ;[:x.app-ui/listen-to-process! :settings/synchronize!]
            :dispatch-n [[:x.app-ui/listen-to-process! request-id]
                        ;[:x.app-ui/set-header-title!  :settings]
                         [:x.app-ui/set-header-title!  extension-id]
                        ;[:x.app-ui/set-window-title!  :settings]
                         [:x.app-ui/set-window-title!  extension-id]
                        ;[:settings/render!]
                         [render-event]]})))

(a/reg-event-fx
  :view-selector/add-routes!
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;
  ; @usage
  ;  [:view-selector/add-routes! :settings {...}]
  (fn [_ [_ extension-id selector-props]]
      (let [route-id          (keyword extension-id :route)
            extended-route-id (keyword extension-id :extended-route)
            extension-name    (name    extension-id)]
                        ;[:x.app-router/add-route! :settings/route
           {:dispatch-n [[:x.app-router/add-route! route-id
                                                   ;:route-template "/settings"
                                                   {:route-template (str "/" extension-name)
                                                   ;:route-event    [:view-selector/load! :settings {...}]
                                                    :route-event    [:view-selector/load! extension-id selector-props]
                                                    :restricted?    true}]
                        ;[:x.app-router/add-route! :settings/extended-route
                         [:x.app-router/add-route! extended-route-id
                                                   ;:route-template "/settings/:selected-view"
                                                   {:route-template (str "/" extension-name "/:selected-view")
                                                   ;:route-event    [:view-selector/load! :settings {...}]
                                                    :route-event    [:view-selector/load! extension-id selector-props]
                                                    :restricted?    true}]]})))
