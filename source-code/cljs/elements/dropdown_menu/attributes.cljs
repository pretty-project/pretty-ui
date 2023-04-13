
(ns elements.dropdown-menu.attributes
    (:require [elements.dropdown-menu.state :as dropdown-menu.state]
              [pretty-css.api               :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-surface-body-attributes
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ {:keys [surface]}]
  (-> {:class :e-dropdown-menu--surface-body}
      (pretty-css/border-attributes surface)
      (pretty-css/color-attributes  surface)
      (pretty-css/indent-attributes surface)))

(defn menu-surface-attributes
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ {:keys [surface]}]
  (-> {:class :e-dropdown-menu--surface}
      (pretty-css/outdent-attributes surface)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-body-attributes
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [style] :as menu-props}]
  (-> {:class :e-dropdown-menu--body
       :style style}
      (pretty-css/indent-attributes menu-props)))

(defn menu-attributes
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;
  ; @return (map)
  ; {}
  [menu-id menu-props]
  ; BUG#7016
  ; - Using the on-mouse-out event means that the child elements of the dropdown menu
  ;   such as the items in the menu-bar element could trigger their parent's on-mouse-out
  ;   event through bubbling.
  ; - Moving the cursor over the menu bar could cause flickering by firing the on-mouse-out
  ;   events of the inner child elements.
  ; - Using the stopPropagation function is not a solution because in case of the mouse
  ;   leaves the dropdown-menu directly from the menu-bar (no padding around the menu-bar)
  ;   means the dropdown-menu on-mouse-out doesn't fire at all.
  ; - https://www.geeksforgeeks.org/how-to-disable-mouseout-events-triggered-by-child-elements
  ;   According to this article using on-mouse-leave instead of the on-mouse-out event
  ;   solves the problem.
  (-> {:class :e-dropdown-menu
       :on-mouse-leave #(swap! dropdown-menu.state/MENUS assoc-in [menu-id :active-dex] nil)}
      (pretty-css/default-attributes menu-props)
      (pretty-css/outdent-attributes menu-props)))
