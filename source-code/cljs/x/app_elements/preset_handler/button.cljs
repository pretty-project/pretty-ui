
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.preset-handler.button)



;; -- Presets -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
;  XXX#8671
;
; A {:layout :icon-button} presetek nem tartalmazzák a :tooltip és :label tulajdonságokat,
; mert az a felhasználás helyén dől el, hogy egy ikon gomb felirattal vagy anélkül jelenik meg.
(def BUTTON-PROPS-PRESETS
     {; Defaults:
      :default-icon-button {:color   :default
                            :layout  :icon-button
                            :variant :transparent}
      :default-button      {:color   :default
                            :horizontal-align :left
                            :layout  :row
                            :variant :transparent}
      :muted-button        {:color   :muted
                            :horizontal-align :left
                            :layout  :row
                            :variant :transparent}
      :muted-icon-button   {:color   :muted
                            :layout  :icon-button
                            :variant :transparent}
      :primary-button      {:color   :primary
                            :horizontal-align :left
                            :layout  :row
                            :variant :transparent}
      :primary-icon-button {:color   :primary
                            :layout  :icon-button
                            :variant :transparent}
      :secondary-button    {:color   :secondary
                            :horizontal-align :left
                            :layout  :row
                            :variant :transparent}
      :secondary-icon-button {:color  :secondary
                              :layout  :icon-button
                              :variant :transparent}
      :success-icon-button {:color   :success
                            :layout  :icon-button
                            :variant :transparent}
      :warning-button      {:color   :warning
                            :horizontal-align :left
                            :layout  :row
                            :variant :transparent}
      :warning-icon-button {:color   :warning
                            :layout  :icon-button
                            :variant :transparent}
      ; *****
      :accept-button       {:color   :primary
                            :label   :accept!
                            :layout  :row
                            :variant :transparent}
      :action-button       {:color   :primary
                            :layout  :row
                            :variant :transparent}
      :add-icon-button     {:color   :primary
                            :icon    :add
                            :layout  :icon-button
                            :variant :transparent}
      :add-to-favorites-icon-button {:color   :secondary
                                     :icon    :favorite_border
                                     :layout  :icon-button
                                     :variant :transparent}
      :added-to-favorites-icon-button {:color   :secondary
                                       :icon    :favorite
                                       :layout  :icon-button
                                       :variant :transparent}
      :apps-icon-button    {:color   :default
                            :icon    :apps
                            :layout  :icon-button
                            :variant :transparent}
      :archive-icon-button {:color   :default
                            :icon    :inventory_2
                            :icon-family :material-icons-outlined
                            :layout  :icon-button
                            :variant :transparent}
      :archived-icon-button {:color   :default
                             :icon    :inventory_2
                             :layout  :icon-button
                             :variant :transparent}
      :back-button         {:color   :default
                            :icon    :arrow_back
                            :layout  :row
                            :variant :transparent}
      :back-icon-button    {:color   :default
                            :icon    :arrow_back
                            :layout  :icon-button
                            :variant :transparent}
      :cancel-button       {:color   :default
                            :label   :cancel!
                            :layout  :row
                            :variant :transparent}
      :cancel-icon-button  {:color   :default
                            :icon    :close
                            :layout  :icon-button
                            :variant :transparent}
      :checked-icon-button {:color   :default
                            :icon    :check_box
                            :layout  :icon-button
                            :variant :transparent}
      :unchecked-icon-button {:color   :default
                              :icon    :check_box_outline_blank
                              :layout  :icon-button
                              :variant :transparent}
      :close-button        {:label   :close!
                            :layout  :row
                            :variant :transparent}
      :close-icon-button   {:color   :default
                            :icon    :close
                            :layout  :icon-button
                            :variant :transparent}
      :delete-button       {:color   :warning
                            :label   :delete!
                            :layout  :row
                            :variant :transparent}
      :delete-icon-button  {:color   :warning
                            :icon    :delete_outline
                            :layout  :icon-button
                            :variant :transparent}
      :duplicate-button {:color   :default
                         :label   :duplicate!
                         :layout  :row
                         :variant :transparent}
      :duplicate-icon-button {:color   :default
                              :icon    :content_copy
                              :layout  :icon-button
                              :variant :transparent}
      :filters-visible-icon-button {:color   :default
                                    :icon    :filter_alt
                                    :layout  :icon-button
                                    :variant :transparent}
      :filters-nonvisible-icon-button {:color   :default
                                       :icon    :filter_alt
                                       :icon-family :material-icons-outlined
                                       :layout  :icon-button
                                       :variant :transparent}
      :forward-icon-button {:color   :default
                            :icon    :arrow_forward
                            :layout  :icon-button
                            :variant :transparent}
      :fullscreen-icon-button {:color   :default
                               :icon    :fullscreen
                               :layout  :icon-button
                               :variant :transparent}
      :help-button         {:color   :default
                            :horizontal-align :left
                            :icon    :help_outline
                            :label   :help
                            :layout  :row
                            :variant :transparent}
      :home-icon-button    {:color   :default
                            :icon    :home
                            :layout  :icon-button
                            :variant :transparent}
      :language-button     {:color   :default
                            :horizontal-align :left
                            :icon    :translate
                            :label   :language
                            :layout  :row
                            :variant :transparent}
      :logout-button       {:color   :warning
                            :horizontal-align :left
                            :icon    :logout
                            :label   :logout!
                            :layout  :row
                            :variant :transparent}
      :menu-icon-button    {:color   :default
                            :icon    :more_vert
                            :layout  :icon-button
                            :variant :transparent}
      :menu-bar-icon-button {:color   :default
                             :icon    :more_horiz
                             :layout  :icon-button
                             :variant :transparent}
      :more-options-button {:color   :default
                            :horizontal-align :left
                            :icon    :list
                            :label   :more-options
                            :layout  :row
                            :variant :transparent}
      :next-icon-button    {:color   :default
                            :icon    :arrow_forward_ios
                            :layout  :icon-button
                            :variant :transparent}
      :prev-icon-button    {:color   :default
                            :icon    :arrow_back_ios
                            :layout  :icon-button
                            :variant :transparent}
      :reorder-mode-icon-button {:color   :default
                                 :icon    :reorder
                                 :layout  :icon-button
                                 :variant :transparent}
      :restore-button      {:color   :default
                            :label   :restore!
                            :layout  :row
                            :variant :transparent}
      :save-button         {:color   :primary
                            :label   :save!
                            :layout  :row
                            :variant :transparent}
      :save-icon-button    {:color   :primary
                            :icon    :save
                            :layout  :icon-button
                            :variant :transparent}
      :search-icon-button  {:color   :default
                            :icon    :search
                            :layout  :icon-button
                            :variant :transparent}
      :select-button       {:label   :select
                            :layout  :row
                            :variant :transparent}
      :select-mode-icon-button {:color   :default
                                :icon    :check_box_outline_blank
                                :layout  :icon-button
                                :variant :transparent}
      :settings-button     {:color   :default
                            :horizontal-align :left
                            :icon    :settings
                            :label   :settings
                            :layout  :row
                            :variant :transparent}
      :order-by-icon-button    {:color   :default
                                :icon    :sort
                                :layout  :icon-button
                                :variant :transparent}
      :up-icon-button        {:color   :default
                              :icon    :chevron_left
                              :layout  :icon-button
                              :variant :transparent}
      :upload-button       {:color   :primary
                            :label   :upload!
                            :layout  :row
                            :variant :transparent}
      :user-menu-icon-button {:color   :default
                              :icon    :account_circle
                              :layout  :icon-button
                              :variant :transparent}})
