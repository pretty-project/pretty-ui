
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.delete-handler.engine
    (:require [mid-fruits.candy      :refer [param]]
              [x.app-components.api  :as components]
              [x.app-core.api        :as a]
              [x.app-environment.api :as environment]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name deletable
;  Olyan elem, amelyet csoportban megjelenítve lehetséges eltávolítani a csoportból.



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn deletable-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:disabled? (boolean)(opt)
  ;   :on-delete (metamorphic-event)}
  ;
  ; @return (map)
  ;  {:disabled (boolean)
  ;   :on-click (function)
  ;   :on-mouse-up (function)
  ;   :title (metamorphic-content)}
  [element-id {:keys [disabled? on-delete]}]
  (if disabled? {:disabled     (param true)}
                {:on-click    #(a/dispatch on-delete)
                 :on-mouse-up #(environment/blur-element!)
                 :title        (components/content {:content :remove!})}))
