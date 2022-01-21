
(ns app-extensions.media.file-bin
    (:require [mid-fruits.candy     :refer [param]]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [app-extensions.media.engine       :as engine]
              [app-extensions.media.file-storage :as file-storage]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- bin-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) bin-props
  ;
  ; @return (map)
  [bin-props]
  (merge {}
         (param bin-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  {})

(a/reg-sub :file-bin/get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-bin-label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [component-id view-props])

(defn- file-bin
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [_ _]
  [:<> [elements/text {:content :will-be-deleted-after :color :muted :font-size :xs}]
       [elements/horizontal-line      {:color :highlight}]
       [elements/horizontal-separator {:size :m}]])

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ;
  ; @return (hiccup)
  [_])
  ;[elements/box {:content    #'file-bin
  ;               :min-width  :xxl
  ;               :subscriber [:file-bin/get-view-props]])

(defn- ghost-file-bin
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [component-id view-props])

(defn- ghost-view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ;
  ; @return (hiccup)
  [_])
  ;[elements/box {:content    #'ghost-file-bin
  ;               :min-width  :xxl
  ;               :subscriber [:file-bin/get-view-props]])

(defn- listener
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @return (component)
  [_])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :file-bin/render!
  ; @param (keyword)(opt) bin-id
  ; @param (map) bin-props
  ;  {}
  (fn [{:keys [db]} event-vector]
      (let [bin-id    (a/event-vector->second-id   event-vector)
            bin-props (a/event-vector->first-props event-vector)
            bin-props (bin-props-prototype bin-props)]
           [:ui/set-surface! :media/view
                             {:view {:content #'view}}])))
