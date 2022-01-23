
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.12.10
; Description:
; Version: v0.3.6
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.icon-button-presets)



;; -- Presets -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
;  XXX#8672
;
; A presetek nem tartalmazzák a :tooltip és :label tulajdonságokat, mert az a felhasználás helyén
; dől el, hogy egy ikon gomb felirattal vagy anélkül jelenik meg.
(def BUTTON-PROPS-PRESETS
     {:default            {:color   :default
                           :layout  :icon-button
                           :variant :transparent}
      :muted              {:color   :muted
                           :layout  :icon-button
                           :variant :transparent}
      :primary            {:color   :primary
                           :layout  :icon-button
                           :variant :transparent}
      :secondary          {:color  :secondary
                           :layout  :icon-button
                           :variant :transparent}
      :success            {:color   :success
                           :layout  :icon-button
                           :variant :transparent}
      :warning            {:color   :warning
                           :layout  :icon-button
                           :variant :transparent}
      :add                {:color   :primary
                           :icon    :add
                           :layout  :icon-button
                           :variant :transparent}
      :add-to-favorites   {:color   :secondary
                           :icon    :favorite_border
                           :layout  :icon-button
                           :variant :transparent}
      :added-to-favorites {:color   :secondary
                           :icon    :favorite
                           :layout  :icon-button
                           :variant :transparent}
      :apps               {:color   :default
                           :icon    :apps
                           :layout  :icon-button
                           :variant :transparent}
      :archive            {:color   :default
                           :icon    :inventory_2
                           :icon-family :material-icons-outlined
                           :layout  :icon-button
                           :variant :transparent}
      :archived           {:color   :default
                           :icon    :inventory_2
                           :layout  :icon-button
                           :variant :transparent}
      :back               {:color   :default
                           :icon    :arrow_back
                           :layout  :icon-button
                           :variant :transparent}
      :cancel             {:color   :default
                           :icon    :close
                           :layout  :icon-button
                           :variant :transparent}
      :checked            {:color   :default
                           :icon    :check_box
                           :layout  :icon-button
                           :variant :transparent}
      :unchecked          {:color   :default
                           :icon    :check_box_outline_blank
                           :layout  :icon-button
                           :variant :transparent}
      :close              {:color   :default
                           :icon    :close
                           :layout  :icon-button
                           :variant :transparent}
      :delete             {:color   :warning
                           :icon    :delete_outline
                           :layout  :icon-button
                           :variant :transparent}
      :duplicate          {:color   :default
                           :icon    :content_copy
                           :layout  :icon-button
                           :variant :transparent}
      :filters-visible    {:color   :default
                           :icon    :filter_alt
                           :layout  :icon-button
                           :variant :transparent}
      :filters-nonvisible {:color   :default
                           :icon    :filter_alt
                           :icon-family :material-icons-outlined
                           :layout  :icon-button
                           :variant :transparent}
      :forward            {:color   :default
                           :icon    :arrow_forward
                           :layout  :icon-button
                           :variant :transparent}
      :fullscreen         {:color   :default
                           :icon    :fullscreen
                           :layout  :icon-button
                           :variant :transparent}
      :home               {:color   :default
                           :icon    :home
                           :layout  :icon-button
                           :variant :transparent}
      :menu               {:color   :default
                           :icon    :more_vert
                           :layout  :icon-button
                           :variant :transparent}
      :menu-bar           {:color   :default
                           :icon    :more_horiz
                           :layout  :icon-button
                           :variant :transparent}
      :next               {:color   :default
                           :icon    :arrow_forward_ios
                           :layout  :icon-button
                           :variant :transparent}
      :placeholder        {:layout  :placeholder                           
                           :variant :transparent}
      :prev               {:color   :default
                           :icon    :arrow_back_ios
                           :layout  :icon-button
                           :variant :transparent}
      :reorder-mode       {:color   :default
                           :icon    :reorder
                           :layout  :icon-button
                           :variant :transparent}
      :save               {:color   :primary
                           :icon    :save
                           :layout  :icon-button
                           :variant :transparent}
      :search             {:color   :default
                           :icon    :search
                           :layout  :icon-button
                           :variant :transparent}
      :select-mode        {:color   :default
                           :icon    :check_box_outline_blank
                           :layout  :icon-button
                           :variant :transparent}
      :order-by           {:color   :default
                           :icon    :sort
                           :layout  :icon-button
                           :variant :transparent}
      :user-menu          {:color   :default
                           :icon    :account_circle
                           :layout  :icon-button
                           :variant :transparent}})
