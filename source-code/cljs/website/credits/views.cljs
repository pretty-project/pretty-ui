
(ns website.credits.views
    (:require [elements.api                  :as elements]
              [random.api                    :as random]
              [re-frame.api                  :as r]
              [website.copyright-label.views :as copyright-label.views]
              [website.credits.prototypes    :as credits.prototypes]
              [website.mt-logo.views         :as mt-logo.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- created-by-label
  ; @param (keyword) component-id
  ; @param (map) component-props
  ; {:color (keyword or string)}
  [_ {:keys [color]}]
  [elements/label ::created-by-label
                  {:color     color
                   :content   "Created by"
                   :font-size :xs
                   :outdent   {:top :xxs}}])

(defn- created-by
  ; @param (keyword) component-id
  ; @param (map) component-props
  [component-id component-props]
  [elements/toggle ::created-by
                   {:content  [:div {:style {:align-items "center" :display "flex" :flex-direction "column"}}
                                    [mt-logo.views/component component-id component-props]
                                    [created-by-label        component-id component-props]]
                    :on-click {:fx [:x.environment/open-tab! "https://www.monotech.hu"]}
                    :outdent  {:bottom :xxs}}])

(defn- credits
  ; @param (keyword) component-id
  ; @param (map) component-props
  ; {:style (map)(opt)}
  [component-id {:keys [style] :as component-props}]
  [:div {:style style}
        [:div {:style {:display "flex" :justify-content "center"}}
              [created-by component-id component-props]]
        [copyright-label.views/component component-id component-props]])

(defn component
  ; @param (keyword)(opt) component-id
  ; @param (map) component-props
  ; {:color (keyword or string)(opt)
  ;   Default: :inherit
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;   :light, :dark
  ;   Default: :light}
  ;
  ; @usage
  ; [credits {...}]
  ;
  ; @usage
  ; [credits :my-credits {...}]
  ([component-props]
   [component (random/generate-keyword) component-props])

  ([component-id component-props]
   (let [component-props (credits.prototypes/component-props-prototype component-props)]
        [credits component-id component-props])))
