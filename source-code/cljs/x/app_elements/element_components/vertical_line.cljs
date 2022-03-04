
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.vertical-line
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.css            :as css]
              [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- line-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) line-id
  ; @param (map) line-props
  ;  {:strength (px)}
  ;
  ; @return (map)
  ;  {:style (map)
  ;    {:width (string)}}
  [_ {:keys [strength] :as line-props}]
  {:style {:width (css/px strength)}})



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- line-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) line-props
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :layout (keyword)
  ;   :strength (px)}
  [line-props]
  (merge {:color    :muted
          :layout   :fit
          :strength 1}
         (param line-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; @param (keyword)(opt) line-id
  ; @param (map) line-props
  ;  {:color (keyword)(opt)
  ;    :default, :highlight, :muted, :primary, :secondary, :success, :warning
  ;    Default: :muted
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :fit
  ;   :strength (px)(opt)
  ;    Default: 1}
  ;
  ; @usage
  ;  [elements/vertical-line {...}]
  ;
  ; @usage
  ;  [elements/vertical-line :my-vertical-line {...}]
  ([line-props]
   [element (a/id) line-props])

  ([line-id line-props]
   (let [line-props (line-props-prototype line-props)]
        [:div.x-vertical-line (engine/element-attributes line-id line-props)
                              [:div.x-vertical-line--body (line-body-attributes line-id line-props)]])))
