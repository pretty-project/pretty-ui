
(ns website.created-by-link.views
    (:require [elements.api                       :as elements]
              [random.api                         :as random]
              [re-frame.api                       :as r]
              [website.created-by-link.prototypes :as created-by-link.prototypes]
              [x.app-details                      :as x.app-details]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- created-by-link
  ; @param (keyword) component-id
  ; @param (map) component-props
  ; {:color (keyword or string)
  ;  :style (map)(opt)}
  [_ {:keys [color style]}]
  (let [server-year          @(r/subscribe [:x.core/get-server-year])
        copyright-information (x.app-details/copyright-information server-year)]
       [:div {:style (merge style {:display "flex" :gap "12px" :justify-content "center"})}
             [elements/button ::link
                              {:color     color
                               :font-size :xs
                               :on-click  {:fx [:x.environment/open-tab! "https://www.monotech.hu"]}
                               :label     copyright-information
                               :style     {:opacity ".75"}}]]))

(defn component
  ; @param (keyword)(opt) component-id
  ; @param (map) component-props
  ; {:color (keyword or string)(opt)
  ;   Default: :inherit
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [created-by-link {...}]
  ;
  ; @usage
  ; [created-by-link :my-created-by-link {...}]
  ([component-props]
   [component (random/generate-keyword) component-props])

  ([component-id component-props]
   (let [component-props (created-by-link.prototypes/component-props-prototype component-props)]
        [created-by-link component-id component-props])))
