
(ns templates.item-lister.wrapper.views
    (:require [x.components.api :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn compact-wrapper
  ; @param (keyword) lister-id
  ; @param (map) wrapper-props
  ; {:body (metamorphic content)
  ;  :footer (metamorphic content)
  ;  :header (metamorphic content)}
  ;
  ; @usage
  ; [compact-wrapper :my-lister {...}]
  [lister-id {:keys [body footer header]}]
  [:div#t-item-lister {:data-layout :compact}
                      [x.components/content lister-id header]
                      [:div#t-item-lister--scroll-container {:data-scrollable-y true}
                                                            [x.components/content lister-id body]
                                                            [x.components/content lister-id footer]]])

(defn wide-wrapper
  ; @param (keyword) lister-id
  ; @param (map) wrapper-props
  ; {:body (metamorphic content)
  ;  :footer (metamorphic content)
  ;  :header (metamorphic content)}
  ;
  ; @usage
  ; [wide-wrapper :my-lister {...}]
  [lister-id {:keys [body footer header sticky]}]
  [:div#t-item-lister {:data-layout :wide}
                      [x.components/content lister-id header]
                      [x.components/content lister-id body]
                      [x.components/content lister-id footer]])
