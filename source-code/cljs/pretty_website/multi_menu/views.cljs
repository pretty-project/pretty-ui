
(ns pretty-website.multi-menu.views
    (:require [fruits.random.api                    :as random]
              [pretty-elements.api                  :as pretty-elements]
              [pretty-elements.engine.api                    :as pretty-elements.engine]
              [pretty-presets.api                   :as pretty-presets]
              [pretty-website.multi-menu.attributes :as multi-menu.attributes]
              [pretty-website.multi-menu.prototypes :as multi-menu.prototypes]
              [pretty-website.sidebar.views         :as sidebar.views]
              [reagent.api                          :as reagent]
              [window-observer.api                  :as window-observer]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sidebar-menu
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  [menu-id menu-props]
  [:div {:class :pw-multi-menu--sidebar-menu-wrapper}
        [pretty-elements/menu-bar (assoc menu-props :orientation :vertical)]])

(defn- multi-menu
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:threshold (px)}
  [menu-id {:keys [threshold] :as menu-props}]
  (if (window-observer/viewport-width-min? threshold)
      [pretty-elements/dropdown-menu menu-id menu-props]
      [sidebar.views/component       menu-id {:content [sidebar-menu menu-id menu-props]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- component-lifecycles
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  [menu-id menu-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    menu-id menu-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount menu-id menu-props))
                       :reagent-render         (fn [_ menu-props] [multi-menu menu-id menu-props])}))

(defn component
  ; @note
  ; For more information, check out the documentation of the ['dropdown-menu'](/pretty-ui/cljs/pretty-elements/api.html#dropdown-menu) element.
  ;
  ; @description
  ; This component implements the 'dropdown-menu' element and in case of the viewport
  ; width is smaller than the given threshold, it displays the menu items
  ; on a sidebar menu and replaces the menu bar with a menu button.
  ;
  ; @param (keyword)(opt) menu-id
  ; @param (map) menu-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;  :threshold (px)(opt)
  ;   Default: 0}
  ;
  ; @usage
  ; [multi-menu {...}]
  ;
  ; @usage
  ; [multi-menu :my-multi-menu {...}]
  ([menu-props]
   [component (random/generate-keyword) menu-props])

  ([menu-id menu-props]
   ; @note (tutorials#parametering)
   (fn [_ menu-props]
       (let [menu-props (pretty-presets/apply-preset                menu-props)
             menu-props (multi-menu.prototypes/menu-props-prototype menu-props)]
            [component-lifecycles menu-id menu-props]))))
