
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns playground.view-selector.helpers)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [view-id]
  [{:label "Anchors"    :on-click [:router/go-to! "/@app-home/playground/anchors"]    :active? (= view-id :anchors)}
   {:label "Buttons"    :on-click [:router/go-to! "/@app-home/playground/buttons"]    :active? (= view-id :buttons)}
   {:label "Chips"      :on-click [:router/go-to! "/@app-home/playground/chips"]      :active? (= view-id :chips)}
   {:label "Diagrams"   :on-click [:router/go-to! "/@app-home/playground/diagrams"]   :active? (= view-id :diagrams)}
   {:label "Expandable" :on-click [:router/go-to! "/@app-home/playground/expandable"] :active? (= view-id :expandable)}
   {:label "Fields"     :on-click [:router/go-to! "/@app-home/playground/fields"]     :active? (= view-id :fields)}
   {:label "Files"      :on-click [:router/go-to! "/@app-home/playground/files"]      :active? (= view-id :files)}
   {:label "Pickers"    :on-click [:router/go-to! "/@app-home/playground/pickers"]    :active? (= view-id :pickers)}
   {:label "Selectors"  :on-click [:router/go-to! "/@app-home/playground/selectors"]  :active? (= view-id :selectors)}
   {:label "Tables"     :on-click [:router/go-to! "/@app-home/playground/tables"]     :active? (= view-id :tables)}
   {:label "Text"       :on-click [:router/go-to! "/@app-home/playground/text"]       :active? (= view-id :text)}])
