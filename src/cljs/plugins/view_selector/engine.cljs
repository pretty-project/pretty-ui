
(ns plugins.view-selector.engine
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :refer [dissoc-in]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-router.api  :as router]))



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  (a/dispatch [:view-selector/initialize! :settings])
;
; @usage
;  (a/dispatch [:view-selector/initialize! :settings {:default-view :privacy}])
;
; @usage
;  (a/dispatch [:view-selector/initialize! :settings {:default-view  :privacy
;                                                     :allowed-views [:privacy :personal :appearance]}])



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

(defn- get-derived-view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:allowed-views (keywords in vector)(opt)
  ;   :default-view (keyword)(opt)}
  ;
  ; @return (keyword)
  ;  A view-id forrásából (route-path param) származó adat. Annak hiánya esetén a default-view.
  [db [_ extension-id {:keys [allowed-views default-view]}]]
  (if-let [derived-view (r router/get-current-route-path-param db :selected-view)]
          (let [derived-view (keyword derived-view)]
               (if (or (not (vector?          allowed-views))
                       (vector/contains-item? allowed-views derived-view))
                   ; If allowed-views is NOT in use,
                   ; or allowed-views is in use & derived-view is allowed ...
                   (return derived-view)
                   ; If allowed-views is in use & derived view is NOT allowed ...
                   (return default-view)))
          (return default-view)))

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

; @usage
;  [:view-selector/change-view! :settings :privacy]
(a/reg-event-db :view-selector/change-view! change-view!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :view-selector/load!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:allowed-views (keywords in vector)(opt)
  ;   :default-view (keyword)(opt)}
  (fn [{:keys [db]} [_ extension-id selector-props]]
      (let [request-id   (request-id extension-id)
            render-event (keyword    extension-id :render!)
            derived-view (r get-derived-view db extension-id selector-props)]
           {:db         (-> db (dissoc-in [extension-id :selector-meta])
                              ;(assoc-in  [:settings    :selector-meta :selected-view] :privacy)
                               (assoc-in  [extension-id :selector-meta :selected-view] derived-view))
                        ;[:x.app-ui/listen-to-process! :settings/synchronize!]
            :dispatch-n [[:x.app-ui/listen-to-process! request-id]
                        ;[:x.app-ui/set-header-title!  :settings]
                         [:x.app-ui/set-header-title!  extension-id]
                        ;[:x.app-ui/set-window-title!  :settings]
                         [:x.app-ui/set-window-title!  extension-id]
                        ;[:settings/render!]
                         [render-event]]})))

(a/reg-event-fx
  :view-selector/initialize!
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:allowed-views (keywords in vector)(opt)
  ;    Ha a selected-view értéke nem található meg az allowed-views felsorolásban,
  ;    akkor behelyettesítésre kerül a default-view értékével.
  ;   :default-view (keyword)(opt)}
  ;
  ; @usage
  ;  [:view-selector/initialize! :settings]
  ;
  ; @usage
  ;  [:view-selector/initialize! :settings {:default-view :privacy}]
  ;
  ; @usage
  ;  [:view-selector/initialize! :settings {:default-view  :privacy
  ;                                         :allowed-views [:privacy :personal :appearance]}]
  (fn [_ [_ extension-id selector-props]]
      (let [route-id          (keyword extension-id :route)
            extended-route-id (keyword extension-id :extended-route)
            extension-name    (name    extension-id)]
                        ;[:x.app-router/add-route! :settings/route {...}]
           {:dispatch-n [[:x.app-router/add-route! route-id
                                                   ;:route-template "/settings"
                                                   {:route-template (str "/" extension-name)
                                                   ;:route-event    [:view-selector/load! :settings {...}]
                                                    :route-event    [:view-selector/load! extension-id selector-props]
                                                    :restricted?    true}]
                        ;[:x.app-router/add-route! :settings/extended-route {...}]
                         [:x.app-router/add-route! extended-route-id
                                                   ;:route-template "/settings/:selected-view"
                                                   {:route-template (str "/" extension-name "/:selected-view")
                                                   ;:route-event    [:view-selector/load! :settings {...}]
                                                    :route-event    [:view-selector/load! extension-id selector-props]
                                                    :restricted?    true}]]})))
