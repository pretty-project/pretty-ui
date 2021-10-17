
(ns extensions.clients.client-form
    (:require [x.app-elements.api :as elements]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-name-fields
  []
  [:<>
     [elements/text-field {:label "First name"}]
     [elements/separator {:size :m :orientation :vertical}]
     [elements/text-field {:label "Last name"}]])

(defn- view
  []
  [:div#clients-client-form
    [elements/button {:label "test"
                      :on-click [:x.app-media/save-file? "data:text/plain;charset=utf-8,abc"]}]
    [elements/select {:options [{:id :xxx :label "As person"}
                                {:id :xxa :label "As company"}]
                      :label "Select!"
                      :value-path [:a :b :s]}]
    [elements/separator {:size :m}]
    [elements/row {:content [:<> [elements/text-field {:label "First name"}]
                                 [elements/separator {:size :m :orientation :vertical}]
                                 [elements/text-field {:label "Last name"}]]}]])
