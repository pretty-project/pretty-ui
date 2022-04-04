
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.preset-handler.icon-button)



;; -- Presets -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
;  XXX#8672
;
; A presetek nem tartalmazzák a :tooltip és :label tulajdonságokat, mert az a felhasználás helyén
; dől el, hogy egy ikon gomb felirattal vagy anélkül jelenik meg.
(def BUTTON-PROPS-PRESETS
     {:default            {:color   :default
                           :variant :transparent}
      :muted              {:color   :muted
                           :variant :transparent}
      :primary            {:color   :primary
                           :variant :transparent}
      :secondary          {:color  :secondary
                           :variant :transparent}
      :success            {:color   :success
                           :variant :transparent}
      :warning            {:color   :warning
                           :variant :transparent}
      :add                {:color   :primary
                           :icon    :add
                           :variant :transparent}
      :add-to-favorites   {:color   :secondary
                           :icon    :favorite_border
                           :variant :transparent}
      :added-to-favorites {:color   :secondary
                           :icon    :favorite
                           :variant :transparent}
      :apps               {:color   :default
                           :icon    :apps
                           :variant :transparent}
      :archive            {:color   :default
                           :icon    :inventory_2
                           :icon-family :material-icons-outlined
                           :variant :transparent}
      :archived           {:color   :default
                           :icon    :inventory_2
                           :variant :transparent}
      :back               {:color   :default
                           :icon    :arrow_back
                           :variant :transparent}
      :cancel             {:color   :default
                           :icon    :close
                           :variant :transparent}
      :checked            {:color   :default
                           :icon    :check_box
                           :variant :transparent}
      :unchecked          {:color   :default
                           :icon    :check_box_outline_blank
                           :variant :transparent}
      :close              {:color   :default
                           :icon    :close
                           :variant :transparent}
      :delete             {:color   :warning
                           :icon    :delete_outline
                           :variant :transparent}
      :duplicate          {:color   :default
                           :icon    :content_copy
                           :variant :transparent}
      :filters-visible    {:color   :default
                           :icon    :filter_alt
                           :variant :transparent}
      :filters-nonvisible {:color   :default
                           :icon    :filter_alt
                           :icon-family :material-icons-outlined
                           :variant :transparent}
      :forward            {:color   :default
                           :icon    :arrow_forward
                           :variant :transparent}
      :fullscreen         {:color   :default
                           :icon    :fullscreen
                           :variant :transparent}
      :home               {:color   :default
                           :icon    :home
                           :variant :transparent}
      :menu               {:color   :default
                           :icon    :more_vert
                           :variant :transparent}
      :menu-bar           {:color   :default
                           :icon    :more_horiz
                           :variant :transparent}
      :next               {:color   :default
                           :icon    :arrow_forward_ios
                           :variant :transparent}
      :placeholder        {:layout  :placeholder
                           :variant :transparent}
      :prev               {:color   :default
                           :icon    :arrow_back_ios
                           :variant :transparent}
      :reorder-mode       {:color   :default
                           :icon    :reorder
                           :variant :transparent}
      :restore            {:color   :default
                           :icon    :restart_alt
                           :variant :transparent}
      :save               {:color   :primary
                           :icon    :save
                           :variant :transparent}
      :search             {:color   :default
                           :icon    :search
                           :variant :transparent}
      :select-mode        {:color   :default
                           :icon    :check_box_outline_blank
                           :variant :transparent}
      :order-by           {:color   :default
                           :icon    :sort
                           :variant :transparent}
      :user-menu          {:color   :default
                           :icon    :account_circle
                           :variant :transparent}})
