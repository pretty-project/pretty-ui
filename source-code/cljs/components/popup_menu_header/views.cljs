
(ns components.popup-menu-header.views
    (:require [components.popup-menu-header.prototypes :as popup-menu-header.prototypes]
              [pretty-elements.api                     :as pretty-elements]
              [random.api                              :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-menu-header
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; {:close-button (map)(opt)
  ;  :label (map)(opt)}
  [_ {:keys [close-button label]}]
  [:div {:class :c-popup-menu-header :data-orientation :horizontal :data-horizontal-row-align :space-between}
        (if label        [pretty-elements/label ::header-label label]
                         [:div {:class :c-popup-menu-header--label-placeholder}])
        (if close-button [pretty-elements/icon-button ::close-button close-button])])

(defn component
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; {:close-button (map)(opt)
  ;   Default: {:border-radius {:all :s}
  ;             :hover-color   :highlight
  ;             :icon          :close
  ;             :keypress      {:key-code 27}}
  ;  :label (map)(opt)
  ;   Default: {:color     :muted
  ;             :font-size :xs
  ;             :indent    {:horizontal :xs :vertical :s}}}
  ;
  ; @usage
  ; [popup-menu-header {...}]
  ;
  ; @usage
  ; [popup-menu-header :my-popup-menu-header {...}]
  ([header-props]
   [component (random/generate-keyword) header-props])

  ([header-id header-props]
   (fn [_ header-props] ; XXX#0106 (README.md#parametering)
       (let [header-props (popup-menu-header.prototypes/header-props-prototype header-props)]
            [popup-menu-header header-id header-props]))))
