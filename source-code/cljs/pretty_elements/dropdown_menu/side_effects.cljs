
(ns pretty-elements.dropdown-menu.side-effects
    (:require [pretty-elements.engine.api :as pretty-elements.engine]
              [pretty-elements.surface.side-effects :as surface.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-dropdown-content!
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (metamorphic-content) dropdown-content
  [menu-id dropdown-content]
  (let [surface-id (pretty-elements.engine/element-id->subitem-id menu-id :surface)]
       (surface.side-effects/set-surface-content! surface-id dropdown-content)))
